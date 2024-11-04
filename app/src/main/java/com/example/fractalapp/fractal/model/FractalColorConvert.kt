package com.example.fractalapp.fractal.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.toArgb
import com.example.fractalapp.db.BitmapConverter
import com.example.fractalapp.ui.theme.FractalTheme
import kotlinx.coroutines.yield

object FractalColorConvert {
    suspend fun convert(icon: String, useColors: Boolean): String {
        if (useColors) return icon

        println("FractalColorConvert")

        val bmp = BitmapConverter.toBitmap(icon)!!
        val res = bmp.copy(Bitmap.Config.ARGB_8888, true)

        val pixels = IntArray(res.width * res.height)
        res.getPixels(pixels, 0, res.width, 0, 0, res.width, res.height)

        for (i in pixels.indices) {
            if (pixels[i] != 0x00000000) {
                pixels[i] = FractalTheme.FractalColor.toArgb()
            }
            if (i % 10000 == 0) yield()
        }

        res.setPixels(pixels, 0, res.width, 0, 0, res.width, res.height)

        return BitmapConverter.fromBitmap(res)!!
    }
}