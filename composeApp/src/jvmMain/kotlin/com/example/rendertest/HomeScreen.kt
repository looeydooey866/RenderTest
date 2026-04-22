package com.example.rendertest

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.onPointerEvent
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        var constants by remember{mutableStateOf(WorldConstants())}

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        var movingFront by remember{mutableStateOf(false)}
        var movingBack by remember{mutableStateOf(false)}
        var movingUp by remember{mutableStateOf(false)}
        var movingDown by remember{mutableStateOf(false)}
        var movingLeft by remember{mutableStateOf(false)}
        var movingRight by remember{mutableStateOf(false)}
        val scope = rememberCoroutineScope()
        val step = 0.1f
        LaunchedEffect(Unit){
            scope.launch {
                while (true){
                    delay(1000 / 24)
                    if (movingFront) {
                        constants = constants.move(dz = step)
                    }
                    if (movingBack) {
                        constants = constants.move(dz = -step)
                    }
                    if (movingUp) {
                        constants = constants.move(dy = step)
                    }
                    if (movingDown) {
                        constants = constants.move(dy = -step)
                    }
                    if (movingLeft) {
                        constants = constants.move(dx = -step)
                    }
                    if (movingRight) {
                        constants = constants.move(dx = step)
                    }
                }
            }
        }

        Scaffold (
            modifier = Modifier.focusRequester(focusRequester).focusable().onKeyEvent{event ->
                if (event.type != KeyEventType.KeyDown && event.type != KeyEventType.KeyUp) return@onKeyEvent false
                when {
                    event.key == Key.A -> {
                        movingLeft = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    event.key == Key.D -> {
                        movingRight = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    event.key == Key.Spacebar -> {
                        movingUp = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    event.key == Key.ShiftLeft -> {
                        movingDown = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    event.key == Key.W -> {
                        movingFront = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    event.key == Key.S -> {
                        movingBack = (event.type == KeyEventType.KeyDown)
                        true
                    }
                    else -> false
                }
            }
        ){
            RenderScreen(
                modifier = Modifier.fillMaxHeight().aspectRatio(16f/9f, true),
                worldConstants = constants,
                shapes = listOf(
                    Cube(
                        0f, 0f, 5f, 1f
                    )
                )
            )
        }
    }
}