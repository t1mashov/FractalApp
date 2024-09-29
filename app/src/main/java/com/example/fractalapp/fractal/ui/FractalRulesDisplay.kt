package com.example.fractalapp.fractal.ui

data class FractalRulesDisplay(
    var rules: String = "",
    var axiom: String = "",
    var angle: String = "",
    var gens: String = "",
    var step: String = "",
    val useColors: Boolean = false,
    val redChannel: Int = 0,
    val greenChannel: Int = 0,
    val blueChannel: Int = 0
)