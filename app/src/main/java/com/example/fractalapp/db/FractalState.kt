package com.example.fractalapp.db

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class FractalState(
    val id: Int = 0,
    val title: MutableState<String>,
    val icon: MutableState<String>,
    val rules: String = "",
    val axiom: String = "",
    val angle: String = "",
    val gens: String = "",
    val step: String = "",
    val useColors: Boolean = false,
    val redChannel: Int = 0,
    val greenChannel: Int = 0,
    val blueChannel: Int = 0
) {
    companion object {
        fun fromFractal(f: Fractal): FractalState {
            return FractalState(
                f.id,
                mutableStateOf(f.title),
                mutableStateOf(f.icon),
                f.rules,
                f.axiom,
                f.angle,
                f.gens,
                f.step,
                f.useColors,
                f.redChannel,
                f.greenChannel,
                f.blueChannel
            )
        }
    }
}