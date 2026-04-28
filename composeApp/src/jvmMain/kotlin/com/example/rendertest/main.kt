package com.example.rendertest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RenderTest",
        state = WindowState(placement = WindowPlacement.Fullscreen)
    ) {
        App()
    }
}