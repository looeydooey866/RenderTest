package com.example.rendertest

import androidx.compose.ui.graphics.Color

data class Triangle(
    val p1: Point,
    val p2: Point,
    val p3: Point,
    val color: Color
){
    val points get() = listOf(p1, p2, p3)

    fun project(width: Int, height: Int, worldConstants: WorldConstants): ProjectedTriangle{
        return ProjectedTriangle(points.map{
            it.project(worldConstants)
        }.map{
            ScreenPoint((it.rx * width).toInt(), (it.ry * height).toInt())
        })
    }

    constructor(points: List<Point>, color: Color): this(p1 = points[0], p2 = points[1], p3 = points[2], color = color){
    }

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): Triangle{
        return Triangle(
            p1.translate(dx, dy, dz),
            p2.translate(dx, dy, dz),
            p3.translate(dx, dy, dz),
            color
        )
    }

    fun normalize(worldConstants: WorldConstants): Triangle{
        return translate(
            -worldConstants.x,
                    -worldConstants.y,
                    -worldConstants.z,
        )
    }
}

data class ProjectedTriangle(
    val a: ScreenPoint,
    val b: ScreenPoint,
    val c: ScreenPoint,
){
    constructor(points: List<ScreenPoint>) : this(points[0], points[1], points[2]) {
    }

    val points get() = listOf(a, b, c)

    fun getBarycentricCoords(p: ScreenPoint): BarycentricCoords{
        val (px, py) = p
        val (ax, ay) = a
        val (bx, by) = b
        val (cx, cy) = c
        val x1 = px - ax
        val x2 = bx - ax
        val x3 = cx - ax
        val y1 = py - ay
        val y2 = by - ay
        val y3 = cy - ay
        fun floatDivide(num: Int, den: Int) = if (den > 0){
            num.toFloat() / den
        }
        else{
            1f
        }
        fun floatDivide(num: Float, den: Int) = if (den > 0){
            num / den
        }
        else{
            1f
        }
        val beta = floatDivide(
            y1 * x3 - y3 * x1,
            y2 * x3 - y3 * x2
        )
        val gamma = floatDivide(
            x1 - beta * x2,
            x3
        )
        val alpha = 1f - beta - gamma
        return BarycentricCoords(
            alpha,
            beta,
            gamma
        )
    }
}