package com.example.fractalapp.fractal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.fractalapp.fractal.FractalViewModel
import com.example.fractalapp.ui.theme.FractalTheme


@Composable
fun ZoomFractal(vm: FractalViewModel) {

    val scale = remember { vm.fractalScale }
    val offset = remember { vm.fractalOffset}

    val bitmap = remember { vm.fractalImage }
    val isLoading = remember { vm.fractalLoading }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FractalTheme.BgColor),
        contentAlignment = Alignment.Center
    ) {

        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        translationX = offset.value.x,
                        translationY = offset.value.y
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, panChange, zoomChange, _ ->

                            scale.value = (scale.value * zoomChange).coerceIn(0.25f, 7f)

                            val extraWith = (scale.value - 1) * constraints.maxWidth
                            val extraHeight = (scale.value - 1) * constraints.maxHeight

                            val maxX = extraWith / 2
                            val maxY = extraHeight / 2

                            offset.value = Offset(
                                x = (offset.value.x + panChange.x * scale.value).coerceIn(
                                    minOf(-maxX, maxX), maxOf(-maxX, maxX)
                                ),
                                y = (offset.value.y + panChange.y * scale.value).coerceIn(
                                    minOf(-maxY, maxY), maxOf(-maxY, maxY)
                                ),
                            )
                        }
                    }
            )
        }


    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(
                color = FractalTheme.Controllers,
                strokeWidth = 5.dp
            )
        }
    }

}