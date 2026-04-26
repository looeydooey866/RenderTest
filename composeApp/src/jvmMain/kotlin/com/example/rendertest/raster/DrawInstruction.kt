package com.example.rendertest.raster

data class PixelPoint(
    val x: Int,
    val y: Int
)

data class DrawInstruction(
    val p: PixelPoint,
    val z: Float,
    val color: Int,
)