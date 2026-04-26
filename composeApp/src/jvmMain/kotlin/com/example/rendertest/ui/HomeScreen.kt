package com.example.rendertest.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import cafe.adriel.voyager.core.screen.Screen
import com.example.rendertest.helper.toRad
import com.example.rendertest.raster.Cube
import com.example.rendertest.raster.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        var constants by remember { mutableStateOf(View(f = 90f)) }

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        var movingFront by remember { mutableStateOf(false) }
        var movingBack by remember { mutableStateOf(false) }
        var movingUp by remember { mutableStateOf(false) }
        var movingDown by remember { mutableStateOf(false) }
        var movingLeft by remember { mutableStateOf(false) }
        var movingRight by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val step = 0.1f
        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    delay(1000 / 60)
                    if (movingFront) {
                        constants = constants.translate(dz = step)
                    }
                    if (movingBack) {
                        constants = constants.translate(dz = -step)
                    }
                    if (movingUp) {
                        constants = constants.translate(dy = step)
                    }
                    if (movingDown) {
                        constants = constants.translate(dy = -step)
                    }
                    if (movingLeft) {
                        constants = constants.translate(dx = -step)
                    }
                    if (movingRight) {
                        constants = constants.translate(dx = step)
                    }
                }
            }
        }

        Scaffold(
            modifier = Modifier.focusRequester(focusRequester).focusable()
                .onKeyEvent { event ->
                    if (event.type != KeyEventType.Companion.KeyDown && event.type != KeyEventType.Companion.KeyUp) return@onKeyEvent false
                    when {
                        event.key == Key.Companion.A -> {
                            movingLeft = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        event.key == Key.Companion.D -> {
                            movingRight = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        event.key == Key.Companion.Spacebar -> {
                            movingUp = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        event.key == Key.Companion.ShiftLeft -> {
                            movingDown = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        event.key == Key.Companion.W -> {
                            movingFront = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        event.key == Key.Companion.S -> {
                            movingBack = (event.type == KeyEventType.Companion.KeyDown)
                            true
                        }

                        else -> false
                    }
                }
        ) {
            RenderScreen(
                modifier = Modifier.Companion.fillMaxHeight().aspectRatio(16f / 9f, true),
                view = constants,
                solids = listOf(
                    Cube( 0f, 0f, 5f, 1f ),
                    Cube( 0f, 1f, 5f, 1f ),
                    Cube( 0f, 2f, 5f, 1f ),
                    Cube( 1f, 1f, 5f, 1f ),
                    Cube( 2f, 0f, 5f, 1f ),
                    Cube( 2f, 1f, 5f, 1f ),
                    Cube( 2f, 2f, 5f, 1f ),
                    Cube( 4f, 0f, 5f, 1f ),
                    Cube( 4f, 2f, 5f, 1f ),
                    Cube( 5f, 0f, 5f, 1f ),
                    Cube( 5f, 1f, 5f, 1f ),
                    Cube( 5f, 2f, 5f, 1f ),
                    Cube( 6f, 0f, 5f, 1f ),
                    Cube( 6f, 2f, 5f, 1f ),
                )
            )
        }
    }
}