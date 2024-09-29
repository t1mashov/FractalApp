package com.example.fractalapp.fractal.model

import android.graphics.Bitmap

interface FractalBuilder {
    fun buildFractal(fr: FractalRules): Bitmap
    fun getResultCode(): Int
}