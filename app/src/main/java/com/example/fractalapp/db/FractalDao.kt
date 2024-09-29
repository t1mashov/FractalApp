package com.example.fractalapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FractalDao {

    @Query("SELECT * FROM ${Fractal.TABLE_NAME}")
    fun getLiked(): LiveData<List<Fractal>>

    @Query("SELECT * FROM ${Fractal.TABLE_NAME} WHERE id = :id")
    fun getLikedById(id: Int): Fractal

    @Insert(entity = Fractal::class, onConflict = OnConflictStrategy.IGNORE)
    fun addFractal(f: Fractal)

    @Query("UPDATE ${Fractal.TABLE_NAME} SET title = :title WHERE id = :id")
    fun updateFractalTitle(id: Int, title: String)

    @Query("DELETE FROM ${Fractal.TABLE_NAME} WHERE id = :id")
    fun deleteFromLiked(id: Int)

}