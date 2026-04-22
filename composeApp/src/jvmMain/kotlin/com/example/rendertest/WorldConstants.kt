package com.example.rendertest

import kotlin.math.tan

data class WorldConstants(
    /**
     * The distance between eye and camera, defaulted to one arm length.
     */
    val ds: Float = 0.5f,
    /**
     * The FOV (field of vision) of the eye.
     */
    val f: Float = 90f.toRad(),
    /**
     * The aspect ratio (w/h) of the screen.
     */
    val rs: Float = 16f/9f,
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
){
    val ws = 2 * tan(f / 2) * ds
    val hs = ws / rs

    fun move(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): WorldConstants{
        return copy(
            x = dx + x,
            y = dy + y,
            z = dz + z
        )
    }
}