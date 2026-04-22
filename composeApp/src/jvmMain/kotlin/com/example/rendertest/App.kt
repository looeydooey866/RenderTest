package com.example.rendertest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource

import rendertest.composeapp.generated.resources.Res
import rendertest.composeapp.generated.resources.compose_multiplatform
import kotlin.math.tan

@Composable
@Preview
fun App() {
    Navigator(HomeScreen())
}
