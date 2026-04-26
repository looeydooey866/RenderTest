package com.example.rendertest.ui

import com.example.rendertest.raster.Camera
import com.example.rendertest.raster.PixelPoint

class LocalBuffer {
    val maxWidth = 3000
    val maxHeight = 2000
    val depthBuf = Array(maxHeight){ FloatArray(maxWidth)}
    fun resetDepthBuf(){
        depthBuf.forEach {
            it.fill(Float.POSITIVE_INFINITY)
        }
    }
    fun updateDepthBuf(point: PixelPoint, z: Float){
        depthBuf[point.y][point.x] = z
    }

    fun ifNearerDepth(point: PixelPoint, z: Float, onValid: (PixelPoint) -> Unit){
        if (depthBuf[point.y][point.x] >= z){
            onValid(point)
        }
    }

    var drawBuf = ByteArray(maxWidth * maxHeight * 4)
    fun draw(camera: Camera, point: PixelPoint, color: Int){
        val width = camera.width.toInt()
        if (point.x !in 0 until width || point.y !in 0 until (maxWidth * maxHeight / width)){
            return
        }
        val index = (point.x + point.y * width) * 4
        drawBuf[index]     = (color and 0xFF).toByte()
        drawBuf[index + 1] = ((color shr 8) and 0xFF).toByte()
        drawBuf[index + 2] = ((color shr 16) and 0xFF).toByte()
        drawBuf[index + 3] = ((color shr 24) and 0xFF).toByte()
    }

    fun resetDrawBuf(){
        for (i in 0..drawBuf.size - 1){
            if (i % 4 == 3){
                drawBuf[i] = 0xFF.toByte()
            }
            else{
                drawBuf[i] = 0
            }
        }
        //drawBuf.fill(0)
    }
}