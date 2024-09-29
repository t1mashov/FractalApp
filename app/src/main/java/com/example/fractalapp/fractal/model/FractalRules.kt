package com.example.fractalapp.fractal.model

import com.example.fractalapp.fractal.ui.FractalRulesDisplay

data class FractalRules(
    var rules: String = "",
    var axiom: String = "",
    var angle: Double = 90.0,
    var gens: Int = 1,
    var step: Int = 5,
    val useColors: Boolean = false,
    val redChannel: Int = 0,
    val greenChannel: Int = 0,
    val blueChannel: Int = 0
)

fun fractalRulesToDisplay(fr: FractalRules): FractalRulesDisplay =
    FractalRulesDisplay(
        fr.rules,
        fr.axiom,
        "${fr.angle.toInt()}",
        "${fr.gens}",
        "${fr.step}",
        fr.useColors,
        fr.redChannel,
        fr.greenChannel,
        fr.blueChannel
    )