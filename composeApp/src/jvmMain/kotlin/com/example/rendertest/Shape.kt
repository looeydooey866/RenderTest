package com.example.rendertest

interface Shape {
    val triangles: List<Triangle>
    fun forEveryTriangle(onTriangle: (Triangle) -> Unit)
}