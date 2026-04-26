package com.example.rendertest.raster

/*
The reason why screenpoint requires a float is because after projecting to the screen, you still need sub-pixel precision for the barycentric coordinates
Sure you can round it to get the pixel that it is in but you need to verify if it is within the triangle, which the barycentric coordinates requires the precise location for and not just the whole number
 */
data class ScreenPoint(
    val x: Float,
    val y: Float,
)

data class BarycentricPoint(
    val alpha: Float,
    val beta: Float,
    val gamma: Float,
){
    fun isInside() = (alpha in 0f..1f && beta in 0f..1f && gamma in 0f..1f)
}