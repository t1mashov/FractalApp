package com.example.fractalapp.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.WorkerThread
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class SampleFractalDatabase(ctx: Context) : SQLiteOpenHelper(
    ctx, DB_NAME, null, DB_VERSION
), SampleFractalDao {

    companion object {

        private const val DB_NAME = "samples.db"
        private const val DB_VERSION = 1
        private const val tableName = "samples"

        @Volatile
        private var INSTANCE: SampleFractalDatabase? = null

        fun getInstance(ctx: Context): SampleFractalDatabase {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = SampleFractalDatabase(ctx)
                    INSTANCE!!
                }
            } else INSTANCE!!
        }
    }


    private fun sampleFromCursor(cursor: Cursor): Fractal =
        Fractal(
            id = cursor.getInt(0),
            title = cursor.getString(1),
            icon = cursor.getString(2),
            rules = cursor.getString(3),
            axiom = cursor.getString(4),
            angle = cursor.getString(5),
            gens = cursor.getString(6),
            useColors = cursor.getInt(7) == 1,
            redChannel = cursor.getInt(8),
            greenChannel = cursor.getInt(9),
            blueChannel = cursor.getInt(10),
            step = cursor.getString(11)
        )

    @WorkerThread
    override fun getSampleById(id: Int): Fractal {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM $tableName WHERE id = ?",
            arrayOf("$id"))
        cursor.moveToFirst()
        val sample = sampleFromCursor(cursor)
        cursor.close()
        return sample
    }

    @WorkerThread
    override fun getSamples(): List<Fractal> {
        try {

            val cursor = readableDatabase.rawQuery("SELECT * FROM $tableName", null)
            cursor.moveToFirst()

            val samples = mutableListOf<Fractal>()
            do {
                samples.add(
                    sampleFromCursor(cursor)
                )
            } while (cursor.moveToNext())

            cursor.close()

            return samples
        }
        catch (e: Exception) {
            return emptyList()
        }
    }



    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun setup(ctx: Context) {
        copyDatabaseFromAssets(ctx)
        Log.d("FRACTAL_DB", "setup()")
    }


    private fun copyDatabaseFromAssets(ctx: Context) {
        val dbPath = ctx.getDatabasePath(DB_NAME)

        if (!dbPath.exists()) {
            dbPath.parentFile?.mkdirs()

            ctx.assets.open(DB_NAME).use { inputStream ->
                FileOutputStream(dbPath).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }
        }
    }

    private fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
    }


}