package com.example.rendertest

data object DegreeConstants{
    const val ROD = Math.PI.toFloat() / 180f
    const val DOR = 180f / Math.PI.toFloat()
}

fun Float.toRad(): Float = this * DegreeConstants.ROD
fun Float.toDeg(): Float = this * DegreeConstants.DOR
