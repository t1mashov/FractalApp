package com.example.fractalapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Fractal::class], version = 1, exportSchema = false)
abstract class FractalDatabase: RoomDatabase() {

    abstract fun fractalDao(): FractalDao

    companion object {
        @Volatile
        private var INSTANCE: FractalDatabase? = null

        fun getDatabase(ctx: Context): FractalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    FractalDatabase::class.java,
                    "fractal_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }

}