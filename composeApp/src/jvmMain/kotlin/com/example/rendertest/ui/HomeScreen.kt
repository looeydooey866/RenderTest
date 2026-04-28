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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import cafe.adriel.voyager.core.screen.Screen
import com.example.rendertest.helper.toRad
import com.example.rendertest.raster.Cube
import com.example.rendertest.raster.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Robot
import java.awt.Toolkit

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        var constants by remember { mutableStateOf(View()) }

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
        val step = 0.5f
        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    delay(1000 / 30)
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
                .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        val change = event.changes.firstOrNull() ?: continue

                        val dx = change.positionChange().x
                        val dy = change.positionChange().y

                        if (dx != 0f){
                            constants = constants.rotate(yaw = -dx / 1000f)
                        }
                        if (dy != 0f){
                            constants = constants.rotate(pitch = dy / 1000f)
                        }
                    }
                }
            }
        ) {
            RenderScreen(
                modifier = Modifier.Companion.fillMaxHeight().aspectRatio(16f / 9f, true),
                view = constants,
                solids = generateSolid(
                    listOf(
                        """
                                #..#.####.#...#....##....####.###...##..##.##...#. #.#####.
                                #..#.#....#...#...#..#...#....#..#.#..#.#.#.#...# #....#...
                                ####.###..#...#...#..#...###..####.#..#.#...#...##.....#...
                                #..#.#....#...#...#..#...#....#.#..#..#.#...#...# #....#...
                                #..#.####.###.###..##....#....#..#..##..#...#...#..#...#...
                            """.trimIndent().split('\n'),
                    )
                )
            )
        }
    }
}

fun generateSolid(layers: List<List<String>>): List<Cube>{
    return layers.mapIndexed{ z, layer ->
        generateCubes(layer, z.toFloat() + 5)
    }.flatten()
}

fun generateCubes(layer: List<String>, z: Float): List<Cube>{
    val res = mutableListOf<Cube>()
    layer.asReversed().forEachIndexed { y, blocks ->
        blocks.forEachIndexed { x, block ->
            if (block != '.'){
                res.add(Cube(x.toFloat(), y.toFloat(), z, 1f))
            }
        }
    }
    return res
}