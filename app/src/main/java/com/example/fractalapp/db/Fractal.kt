package com.example.fractalapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Fractal.TABLE_NAME)
class Fractal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "icon")
    val icon: String,

    @ColumnInfo(name = "rules", defaultValue = "")
    val rules: String = "",

    @ColumnInfo(name = "axiom", defaultValue = "")
    val axiom: String = "",

    @ColumnInfo(name = "angle", defaultValue = "")
    val angle: String = "",

    @ColumnInfo(name = "gens", defaultValue = "")
    val gens: String = "",

    @ColumnInfo(name = "step", defaultValue = "")
    val step: String = "",

    @ColumnInfo(name = "useColors", defaultValue = "false")
    val useColors: Boolean = false,

    @ColumnInfo(name = "redChannel", defaultValue = "0")
    val redChannel: Int = 0,

    @ColumnInfo(name = "greenChannel", defaultValue = "0")
    val greenChannel: Int = 0,

    @ColumnInfo(name = "blueChannel", defaultValue = "0")
    val blueChannel: Int = 0
) {
    companion object {
        const val TABLE_NAME = "fractal"

        fun defaultName(): String = "FRACTAL${System.currentTimeMillis()}"
    }
    override fun toString(): String {
        return "Fractal(id=$id, title=$title, rules=$rules, axiom=$axiom, angle=$angle, gens=$gens, step=$step)"
    }
}