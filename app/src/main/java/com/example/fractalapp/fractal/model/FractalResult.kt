package com.example.fractalapp.fractal.model

class FractalResult {
    companion object {
        const val SUCCESS: Int = 0b0000
        const val EMPTY_SIDE: Int = 0b0001
        const val ROAD_OVERFLOW: Int = 0b0010
        const val BORDER_OVERFLOW: Int = 0b0100
        const val SYNTAX_ERROR: Int = 0b1000
    }
}