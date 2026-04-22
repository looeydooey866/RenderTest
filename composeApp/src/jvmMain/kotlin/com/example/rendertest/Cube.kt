package com.example.rendertest

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.random.nextInt


/*


               e     f
          a     b
               h     g
          d     c

 */

class Cube(x: Float, y: Float, z: Float, size: Float): Shape{
    val hsize = size / 2
    val a = Point(x - hsize, y + hsize, z - hsize)
    val b = Point(x + hsize, y + hsize, z - hsize)
    val c = Point(x + hsize, y - hsize, z - hsize)
    val d = Point(x - hsize, y - hsize, z - hsize)
    val e = Point(x - hsize, y + hsize, z + hsize)
    val f = Point(x + hsize, y + hsize, z + hsize)
    val g = Point(x + hsize, y - hsize, z + hsize)
    val h = Point(x - hsize, y - hsize, z + hsize)
    override val triangles = listOf(
        // front: abcd
        Triangle(a, b, c, Color.Red),
        Triangle(a, c, d, Color.Red),
        // right: bfgc
        Triangle(b, f, g, Color.Blue),
        Triangle(b, g, c, Color.Blue),
        // left: aehd
        Triangle(a, e, h, Color.Green),
        Triangle(a, h, d, Color.Green),
        // top: aefb
        Triangle(a, e, f, Color.Yellow),
        Triangle(a, f, b, Color.Yellow),
        // bottom: dhgc
        Triangle(d, h, g, Color.Cyan),
        Triangle(d, g, c, Color.Cyan),
        // back: efgh
        Triangle(e, f, g, Color.Black),
        Triangle(e, g, h, Color.Black),
    )


    override fun forEveryTriangle(onTriangle: (Triangle) -> Unit) {
        triangles.forEach {
            onTriangle(it)
        }
    }
}