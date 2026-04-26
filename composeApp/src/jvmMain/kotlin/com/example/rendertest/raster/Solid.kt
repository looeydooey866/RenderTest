package com.example.rendertest.raster

interface Solid {
    fun forEveryTriangle(onTriangle: (WorldTriangle) -> Unit)
    fun getTriangles(): List<WorldTriangle>
}