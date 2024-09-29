package com.example.fractalapp.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.Base64

class BitmapConverter {
    companion object {

        @TypeConverter
        fun fromBitmap(bitmap: Bitmap?): String? {
            return bitmap?.let {
                val outputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                Base64.getEncoder().encodeToString(outputStream.toByteArray())
            }
        }

        @TypeConverter
        fun toBitmap(byteArray: String?): Bitmap? {
            return byteArray?.let {
                val arr = Base64.getDecoder().decode(it)
                BitmapFactory.decodeByteArray(
                    arr,
                    0,
                    arr.size)
            }
        }
    }
}
