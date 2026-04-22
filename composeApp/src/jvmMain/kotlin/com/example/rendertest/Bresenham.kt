package com.example.rendertest

import kotlin.math.abs

fun bresenham(p1: ScreenPoint, p2: ScreenPoint, forEveryPoint: (ScreenPoint) -> Unit){
    var (x0, y0) = p1
    var (x1, y1) = p2
    var dx =  abs(x1-x0)
    var sx = if (x0<x1)  1 else -1;
    var dy = -abs(y1-y0)
    var sy = if (y0<y1)  1 else -1;
    var err = dx+dy
    var e2 = 0 /* error value e_xy */

    while (true){
        forEveryPoint(ScreenPoint(x0, y0))
        if (x0==x1 && y0==y1) break;
        e2 = 2*err;
        if (e2 >= dy) { err += dy; x0 += sx; }
        if (e2 <= dx) { err += dx; y0 += sy; }
    }
}