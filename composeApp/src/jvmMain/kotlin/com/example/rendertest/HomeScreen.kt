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
import cafe.adriel.voyager.core.screen.Screen

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        var constants by remember{mutableStateOf(WorldConstants())}
        var triangles = listOf(
            Triangle(
                Point(-1f, -1f, 3f),
                Point(-1f, 1f, 3f),
                Point(1f, -1f, 3f),
                Color.Red
            ),
            Triangle(
                Point(1f, 1f, 3f),
                Point(-1f, 1f, 3f),
                Point(1f, -1f, 3f),
                Color.Blue
            ),
            Triangle(
                Point(1f, 1f, 3f),
                Point(1f, -1f, 3f),
                Point(1f, 1f, 5f),
                Color.Yellow
            ),
            Triangle(
                Point(1f, -1f, 5f),
                Point(1f, -1f, 3f),
                Point(1f, 1f, 5f),
                Color.Green
            ),
        )
        triangles = triangles.map{
            it.copy(
                p1 = it.p1.copy(x = it.p1.x),
                p2 = it.p2.copy(x = it.p2.x),
                p3 = it.p3.copy(x = it.p3.x),
            )
        }

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Scaffold (
            modifier = Modifier.focusRequester(focusRequester).focusable().onKeyEvent{event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                when {
                    event.key == Key.DirectionLeft -> {
                        constants = constants.move(dx = -1f)
                        println("LEFT")
                        true
                    }
                    event.key == Key.DirectionRight -> {
                        constants = constants.move(dx = 1f)
                        println("RIGHT")
                        true
                    }
                    event.key == Key.DirectionUp -> {
                        constants = constants.move(dy = 1f)
                        println("UP")
                        true
                    }
                    event.key == Key.DirectionDown -> {
                        constants = constants.move(dy = -1f)
                        println("DOWN")
                        true
                    }
                    else -> false
                }
            }
        ){
            RenderScreen(
                modifier = Modifier.fillMaxHeight().aspectRatio(16f/9f, true),
                worldConstants = constants,
                triangles = triangles,
            )
        }
    }
}