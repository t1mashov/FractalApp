package com.example.fractalapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.home.FractalListWidgetViewModel


@Composable
fun FractalRow(
    first: Fractal,
    second: Fractal?,
    vm: FractalListWidgetViewModel,
    navController: NavHostController?
) {
    Row (
        modifier = Modifier
            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FractalWidget(
            modifier = Modifier.weight(1f),
            fractal = first,
            vm = vm,
            navController = navController,
        )
        Spacer(modifier = Modifier.padding(5.dp))
        if (second != null) {
            FractalWidget(
                modifier = Modifier.weight(1f),
                fractal = second,
                vm = vm,
                navController = navController,
            )
        }
        else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun FractalListWidget(
    vm: FractalListWidgetViewModel,
    navController: NavHostController?
) {
    vm.getFractalList()?.let {
        for (i in 0..<it.size step 2) {
            FractalRow(
                first = it[i],
                second = if (i+1 < it.size) it[i+1] else null,
                vm = vm,
                navController = navController,
            )
        }

    }
}

