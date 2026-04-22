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

    fun getBarycentricCoords(p: ScreenPoint): BarycentricCoords {
        val (px, py) = p
        val (ax, ay) = a
        val (bx, by) = b
        val (cx, cy) = c

        val det = (by - cy) * (ax - cx) + (cx - bx) * (ay - cy)
        if (det == 0) return BarycentricCoords(1f, 0f, 0f)

        val alpha = ((by - cy) * (px - cx) + (cx - bx) * (py - cy)).toFloat() / det
        val beta = ((cy - ay) * (px - cx) + (ax - cx) * (py - cy)).toFloat() / det
        val gamma = 1f - alpha - beta

        return BarycentricCoords(alpha, beta, gamma)
    }
}