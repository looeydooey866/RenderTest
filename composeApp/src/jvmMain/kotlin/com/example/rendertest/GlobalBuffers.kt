package com.example.rendertest

import androidx.compose.ui.graphics.Color
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

    val drawBuf = Array(maxHeight){ Array<Color>(maxWidth){ Color.Transparent }}
    fun draw(point: ScreenPoint, color: Color){
        drawBuf[point.y][point.x] = color
    }

    fun drawOnScreen(onDraw: (ScreenPoint, Color) -> Unit){
        for (y in 0 until maxHeight){
            for (x in 0 until maxWidth){
                onDraw(ScreenPoint(x, y), drawBuf[y][x])
            }
        }
    }

    fun resetDrawBuf(){
        drawBuf.forEach {
            it.fill(Color.Transparent)
        }
    }
}