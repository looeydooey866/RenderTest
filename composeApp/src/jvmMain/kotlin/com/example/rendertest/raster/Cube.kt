package com.example.rendertest.raster


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


/*


               e     f
          a     b
               h     g
          d     c

 */

class Cube(x: Float, y: Float, z: Float, size: Float): Solid{
    val hsize = size / 2
    val a = WorldPoint(x - hsize, y + hsize, z - hsize)
    val b = WorldPoint(x + hsize, y + hsize, z - hsize)
    val c = WorldPoint(x + hsize, y - hsize, z - hsize)
    val d = WorldPoint(x - hsize, y - hsize, z - hsize)
    val e = WorldPoint(x - hsize, y + hsize, z + hsize)
    val f = WorldPoint(x + hsize, y + hsize, z + hsize)
    val g = WorldPoint(x + hsize, y - hsize, z + hsize)
    val h = WorldPoint(x - hsize, y - hsize, z + hsize)
    // front: abcd
    val fr1 = WorldTriangle(a, b, c, Color.Red.toArgb())
    val fr2 = WorldTriangle(a, c, d, Color.Red.toArgb())
    // right: bfgc
    val ri1 = WorldTriangle(b, f, g, Color.Blue.toArgb())
    val ri2 = WorldTriangle(b, g, c, Color.Blue.toArgb())
    // left: aehd
    val le1 = WorldTriangle(a, e, h, Color.Green.toArgb())
    val le2 = WorldTriangle(a, h, d, Color.Green.toArgb())
    // top: aefb
    val to1 = WorldTriangle(a, e, f, Color.Yellow.toArgb())
    val to2 = WorldTriangle(a, f, b, Color.Yellow.toArgb())
    // bottom: dhgc
    val bo1 = WorldTriangle(d, h, g, Color.Cyan.toArgb())
    val bo2 = WorldTriangle(d, g, c, Color.Cyan.toArgb())
    // back: efgh
    val ba1 = WorldTriangle(e, f, g, Color.Black.toArgb())
    val ba2 = WorldTriangle(e, g, h, Color.Black.toArgb())
    override fun getTriangles() = listOf(
        fr1, fr2, ri1, ri2, le1, le2, to1, to2, bo1, bo2, ba1, ba2
    )


    override fun forEveryTriangle(onTriangle: (WorldTriangle) -> Unit) {
        onTriangle(fr1)
        onTriangle(fr2)
        onTriangle(ri1)
        onTriangle(ri2)
        onTriangle(le1)
        onTriangle(le2)
        onTriangle(to1)
        onTriangle(to2)
        onTriangle(bo1)
        onTriangle(bo2)
        onTriangle(ba1)
        onTriangle(ba2)
    }
}