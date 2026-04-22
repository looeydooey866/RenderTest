package com.example.rendertest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min

@Composable
fun RenderScreen(
    modifier: Modifier = Modifier,
    worldConstants: WorldConstants = WorldConstants(),
    shapes: List<Shape> = listOf()
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier
    ){
        val maxHeight = maxHeight
        val maxWidth = maxWidth
        val height = with(density){maxHeight.toPx()}.toInt()
        val width = with(density){maxWidth.toPx()}.toInt()
        if (height == 0 || width == 0){
            return@BoxWithConstraints
        }
        Canvas(
            modifier = Modifier
            .size(maxWidth, maxHeight)
        ){
            GlobalBuffers.resetDepthBuf()
            GlobalBuffers.resetDrawBuf()
            shapes.forEach { shape ->
                shape.forEveryTriangle { triangle ->
                    val triangle = triangle.normalize(worldConstants)
                    if (
                        triangle.p1.z <= worldConstants.ds + 0.01f ||
                        triangle.p2.z <= worldConstants.ds + 0.01f ||
                        triangle.p3.z <= worldConstants.ds + 0.01f
                            ){
                        return@forEveryTriangle
                    }
                    GlobalBuffers.resetScanlineBuf()
                    val screenPoints = triangle.project(width, height, worldConstants)
                    screenPoints.points.forEachPair{a, b ->
                        bresenham(a, b){ point ->
                            if (point.y !in 0 until height){
                                return@bresenham
                            }
                            GlobalBuffers.updateScanlineBuf(point.copy(x = max(0, min(width - 1, point.x))))
                        }
                    }
                    val depths = triangle.points.map{it.z}
                    val reciprocals = depths.map{1 / it}
                    GlobalBuffers.scan{ y, left, right ->
                        for (x in left..right){
                            val cur = ScreenPoint(x, y)
                            val coords = screenPoints.getBarycentricCoords(cur)
                            val da = coords.a * reciprocals[0]
                            val db = coords.b * reciprocals[1]
                            val dc = coords.c * reciprocals[2]
                            val depth = 1 / (da + db + dc)
                            if (depth >= worldConstants.ds){
                                GlobalBuffers.ifNearerDepth(cur, depth){
                                    GlobalBuffers.draw(width, cur, triangle.color)
                                    GlobalBuffers.updateDepthBuf(cur, depth)
                                }
                            }
                        }
                    }
                }
            }
            val bitmap = Bitmap()
            bitmap.allocPixels(
                ImageInfo(width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL)
            )
            bitmap.installPixels(GlobalBuffers.drawBuf)

            drawImage(bitmap.asComposeImageBitmap())
        }
    }
}