package com.example.rendertest

data class ScreenRatio(
    val rx: Float,
    val ry: Float,
)

data class ScreenPoint(
    val x: Int,
    val y: Int,
)

data class Point(
    val x: Float,
    val y: Float,
    val z: Float,
) {
    companion object{
        val Origin = Point(0f, 0f, 0f)
    }
    fun project(worldConstants: WorldConstants): ScreenRatio{
        val ds = worldConstants.ds
        val ws = worldConstants.ws
        val hs = worldConstants.hs
        val sx = (ds * x) / z
        val sy = (ds * y) / z
        val rx = sx / ws + 0.5f
        val ry = 0.5f - sy / hs
        return ScreenRatio(rx, ry)
    }

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): Point{
        return Point(x + dx, y + dy, z + dz)
    }
}

data class BarycentricCoords(
    val a: Float,
    val b: Float,
    val c: Float,
)