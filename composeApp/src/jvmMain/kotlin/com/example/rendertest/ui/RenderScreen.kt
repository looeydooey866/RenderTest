package com.example.rendertest.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import com.example.rendertest.raster.Solid
import com.example.rendertest.raster.View
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo

@Composable
fun RenderScreen(
    view: View,
    modifier: Modifier = Modifier,
    solids: List<Solid> = listOf()
) {
    val density = LocalDensity.current
    val localBuffer = remember{ LocalBuffer() }
    BoxWithConstraints(
        modifier = modifier
    ){
        val maxHeight = maxHeight
        val maxWidth = maxWidth
        val height = with(density){maxHeight.toPx()}
        val width = with(density){maxWidth.toPx()}
        if (height == 0f || width == 0f){
            return@BoxWithConstraints
        }
        val camera = view.toCamera(width, height)
        Canvas(
            modifier = Modifier
                .size(maxWidth, maxHeight)
        ){
            localBuffer.resetDepthBuf()
            localBuffer.resetDrawBuf()
            solids.forEach { shape ->
                shape.forEveryTriangle { triangle ->
                    triangle.rasterize(
                        camera
                    ){ (cur, depth, c)->
                        localBuffer.ifNearerDepth(cur, depth){
                            localBuffer.draw(camera, cur, c)
                            localBuffer.updateDepthBuf(cur, depth)
                        }
                    }
                }
            }
            val bitmap = Bitmap()
            bitmap.allocPixels(
                ImageInfo(width.toInt(), height.toInt(), ColorType.BGRA_8888, ColorAlphaType.PREMUL)
            )
            bitmap.installPixels(localBuffer.drawBuf)

            drawImage(bitmap.asComposeImageBitmap())
        }
    }
}