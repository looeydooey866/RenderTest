package com.example.rendertest.raster

import com.example.rendertest.helper.toRad
import kotlin.math.tan

/**
 * The class containing the configuration for the current Camera being used.
 * One unit of distance is defined as one meter.
 * The unit used for angles is radians.
 */
data class Camera(
    /**
     * The width of the camera's display in pixels
     */
    val width: Float,
    /**
     * The height of the camera's display in pixels
     */
    val height: Float,
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
    val yaw: Float = 0f,
    val pitch: Float = 0f,
){
    val ws = 2 * tan(f / 2) * ds
    val hs = ws / rs

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): Camera{
        return copy(
            x = dx + x,
            y = dy + y,
            z = dz + z
        )
    }
}

data class View(
    val ds: Float = 0.5f,
    val f: Float = 90f.toRad(),
    val rs: Float = 16f/9f,
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
){
    fun toCamera(width: Float, height: Float) = Camera(width, height, ds, f, rs, x, y, z, yaw, pitch)

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f): View{
        return copy(
            x = dx + x,
            y = dy + y,
            z = dz + z
        )
    }
}