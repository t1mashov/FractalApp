package com.example.fractalapp.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class FractalRepository(
    private val fractalDao: FractalDao,
    private val sampleFractalDao: SampleFractalDao
) {

    fun getLiked() = fractalDao.getLiked()
    fun getLikedById(id: Int) = fractalDao.getLikedById(id)

    fun addFractal(f: Fractal) {
        fractalDao.addFractal(f)
    }
    fun updateFractalTitle(id: Int, name: String) {
        fractalDao.updateFractalTitle(id, name)
    }

    fun deleteFractal(id: Int) {
        fractalDao.deleteFromLiked(id)
    }

    fun getSamples() = sampleFractalDao.getSamples()
    fun getSampleById(id: Int) = sampleFractalDao.getSampleById(id)

}