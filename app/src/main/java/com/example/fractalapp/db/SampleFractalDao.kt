package com.example.fractalapp.db

interface SampleFractalDao {
    fun getSamples(): List<Fractal>
    fun getSampleById(id: Int): Fractal
}