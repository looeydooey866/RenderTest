package com.example.rendertest

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.min

data object GlobalBuffers{
    const val maxWidth = 3000
    const val maxHeight = 2000
    val scanlineMinBuf = IntArray(maxHeight)
    val scanlineMaxBuf = IntArray(maxHeight)
    fun resetScanlineBuf(){
        scanlineMinBuf.fill(Int.MAX_VALUE)
        scanlineMaxBuf.fill(Int.MIN_VALUE)
    }
    fun updateScanlineBuf(point: ScreenPoint){
        val (x,y) = point
        scanlineMinBuf[y] = min(scanlineMinBuf[y], x)
        scanlineMaxBuf[y] = max(scanlineMaxBuf[y], x)
    }
    fun scan(onExistingLine: (Int, Int, Int) -> Unit){
        for (i in 0 until maxHeight){
            val left = scanlineMinBuf[i]
            val right = scanlineMaxBuf[i]
            if (left <= right){
                onExistingLine(i, left, right)
            }
        }
    }

    val depthBuf = Array(maxHeight){ FloatArray(maxWidth)}
    fun resetDepthBuf(){
        depthBuf.forEach {
            it.fill(Float.POSITIVE_INFINITY)
        }
    }
    fun updateDepthBuf(point: ScreenPoint, z: Float){
        depthBuf[point.y][point.x] = z
    }

    fun ifNearerDepth(point: ScreenPoint, z: Float, onValid: (ScreenPoint) -> Unit){
        if (depthBuf[point.y][point.x] >= z){
            onValid(point)
        }
    }

    var drawBuf = ByteArray(maxWidth * maxHeight * 4)
    fun draw(width: Int, point: ScreenPoint, color: Color){
        if (point.x !in 0 until width || point.y !in 0 until (maxWidth * maxHeight / width)){
            return
        }
        val index = (point.x + point.y * width) * 4
        val c = color.toArgb()
        drawBuf[index]     = (c and 0xFF).toByte()
        drawBuf[index + 1] = ((c shr 8) and 0xFF).toByte()
        drawBuf[index + 2] = ((c shr 16) and 0xFF).toByte()
        drawBuf[index + 3] = ((c shr 24) and 0xFF).toByte()
    }

    fun resetDrawBuf(){
        drawBuf.fill(0)
    }
}