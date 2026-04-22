package com.example.rendertest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.max
import kotlin.math.min

@Composable
fun RenderScreen(
    modifier: Modifier = Modifier,
    worldConstants: WorldConstants = WorldConstants(),
    triangles: List<Triangle> = emptyList()
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
            triangles.forEach { triangle ->
                val triangle = triangle.normalize(worldConstants)
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
                        //println("Coords: $coords")
                        //println("Reciprocals: $reciprocals")
                        val da = coords.a * reciprocals[0]
                        val db = coords.b * reciprocals[1]
                        val dc = coords.c * reciprocals[2]
                        val depth = 1 / (da + db + dc)
                        //println("($x $y) -> depth $depth")
                        GlobalBuffers.ifNearerDepth(cur, depth){
                            GlobalBuffers.draw(cur, triangle.color)
                            GlobalBuffers.updateDepthBuf(cur, depth)
                        }
                    }
                }
                GlobalBuffers.drawOnScreen { point, color ->
                    if (point.x in 0 until width){
                        if (point.y in 0 until height){
                            drawRect(
                                color,
                                Offset(point.x.toFloat(), point.y.toFloat()),
                                Size(1f,1f)
                            )
                        }
                    }
                    /*
                    drawPoints(
                        listOf(Offset(point.x.toFloat(), point.y.toFloat())),
                        PointMode.Points,
                        color
                    )
                     */
                }
            }
        }
    }
}