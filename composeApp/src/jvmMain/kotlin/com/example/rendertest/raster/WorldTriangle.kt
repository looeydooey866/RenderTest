package com.example.rendertest.raster

import androidx.compose.ui.util.fastMaxOf
import androidx.compose.ui.util.fastMinOf
import com.example.rendertest.helper.inv
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class WorldPoint(
    val x: Float,
    val y: Float,
    val z: Float,
) {
    fun isBehindCamera(camera: Camera): Boolean{
        return z < camera.ds
    }

    fun project(camera: Camera): ScreenPoint{
        val ds = camera.ds
        val ws = camera.ws
        val hs = camera.hs
        val sx = (ds * x) / z
        val sy = (ds * y) / z
        val rx = sx / ws + 0.5f
        val ry = 0.5f - sy / hs
        return ScreenPoint(rx * camera.width, ry * camera.height)
    }

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): WorldPoint{
        return WorldPoint(x + dx, y + dy, z + dz)
    }

    fun rotate(yaw: Float = 0f, pitch: Float = 0f): WorldPoint{
        val syaw = sin(-yaw)
        val cyaw = cos(-yaw)
        val x1 = x * cyaw - z * syaw
        val z1 = x * syaw + z * cyaw
        val spitch = sin(pitch)
        val cpitch = cos(pitch)
        val z2 = z1 * cpitch - y * spitch
        val y2 = z1 * spitch + y * cpitch
        return WorldPoint(x1, y2, z2)
    }
}

data class WorldTriangle(
    val p1: WorldPoint,
    val p2: WorldPoint,
    val p3: WorldPoint,
    val color: Int
){
    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): WorldTriangle{
        return WorldTriangle(
            p1.translate(dx, dy, dz),
            p2.translate(dx, dy, dz),
            p3.translate(dx, dy, dz),
            color
        )
    }

    fun rotate(yaw: Float = 0f, pitch: Float = 0f): WorldTriangle{
        return WorldTriangle(
            p1.rotate(yaw, pitch),
            p2.rotate(yaw, pitch),
            p3.rotate(yaw, pitch),
            color
        )
    }

    fun normalize(camera: Camera): WorldTriangle{
        return translate(
            -camera.x,
                    -camera.y,
                    -camera.z,
        ).rotate(
            camera.yaw,
            camera.pitch,
        )
    }

    fun undrawable(camera: Camera) = (p1.z < camera.ds && p2.z < camera.ds && p3.z < camera.ds)

    inline fun rasterize(camera: Camera, onDraw: (DrawInstruction) -> Unit){
        with(normalize(camera)){
            if (undrawable(camera)){
                return
            }
            if (p1.z <= (0.00001f) || p2.z <= 0.00001f || p3.z <= 0.00001f){
                return
            }
            val s1 = p1.project(camera)
            val s2 = p2.project(camera)
            val s3 = p3.project(camera)
            val minX = max( 0, fastMinOf(s1.x, s2.x, s3.x, camera.width).toInt() )
            if (minX >= camera.width.toInt()){ return }
            val minY = max( 0, fastMinOf(s1.y, s2.y, s3.y, camera.height).toInt() )
            if (minY >= camera.height.toInt()){ return }
            val maxX = min( camera.width.toInt() - 1, fastMaxOf(s1.x, s2.x, s3.x, 0f).toInt() )
            if (maxX < 0){ return }
            val maxY = min( camera.height.toInt() - 1, fastMaxOf(s1.y, s2.y, s3.y, 0f).toInt() )
            if (maxY < 0){ return }
            for (x in minX..maxX){
                for (y in minY..maxY){
                    assert(x in 0..<camera.width.toInt())
                    assert(y in 0..<camera.height.toInt())
                    //subpixel precision needed here otherwise inaccurate
                    val point = ScreenPoint(x + 0.5f, y + 0.5f)
                    val bp = bary(point, s1, s2, s3)
                    if (bp.isInside()){
                        val zrecip = p1.z.inv() * bp.alpha + p2.z.inv() * bp.beta + p3.z.inv() * bp.gamma
                        val z = zrecip.inv()
                        onDraw(DrawInstruction(
                            PixelPoint(x, y), z, color
                        ))
                    }
                }
            }
        }
    }
}

fun bary(p: ScreenPoint, a: ScreenPoint, b: ScreenPoint, c: ScreenPoint): BarycentricPoint {
    val (px, py) = p
    val (ax, ay) = a
    val (bx, by) = b
    val (cx, cy) = c

    val det = (by - cy) * (ax - cx) + (cx - bx) * (ay - cy)
    if (det == 0f) return BarycentricPoint(1f, 0f, 0f)

    val alpha = ((by - cy) * (px - cx) + (cx - bx) * (py - cy)) / det
    val beta = ((cy - ay) * (px - cx) + (ax - cx) * (py - cy)) / det
    val gamma = 1f - alpha - beta

    return BarycentricPoint(alpha, beta, gamma)
}
