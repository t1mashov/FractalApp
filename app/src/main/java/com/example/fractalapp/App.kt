package com.example.fractalapp

import android.app.Application
import com.example.fractalapp.db.FractalDatabase
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.db.SampleFractalDatabase

class App: Application() {

    val database by lazy { FractalDatabase.getDatabase(this) }
    val sampleDatabase by lazy { SampleFractalDatabase.getInstance(this) }
    val repository by lazy { FractalRepository(
        database.fractalDao(),
        sampleDatabase
    )}

}