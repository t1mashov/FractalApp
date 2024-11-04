package com.example.fractalapp.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.fractalapp.SCREEN_FRACTAL_BUILDER
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.db.FractalState
import kotlinx.coroutines.launch

abstract class FractalListWidgetViewModel(): ViewModel() {

    var selectionId: MutableState<Int?> = mutableStateOf(null)

    abstract fun isSelectionExists(): MutableState<Boolean>
    abstract fun getFractalList(): List<Fractal>?
    abstract fun getFractalStateList(): List<FractalState>?
    abstract fun onClick(navController: NavHostController, id: Int)
    open fun onLongPress(navController: NavHostController, id: Int) {}
    open fun clearSelection() {}
}


class FractalSamplesListWidgetViewModel() : FractalListWidgetViewModel() {

    var fractals: List<Fractal> = emptyList()
        private set
    var fractalStates: SnapshotStateList<FractalState> = mutableStateListOf()
    val isSelected = mutableStateOf(false)

    fun setFractals(fractals: List<Fractal>) {
        this.fractals = fractals
    }
//    fun setFractalStates(fractalStates: SnapshotStateList<FractalState>) {
//        this.fractalStates = fractalStates
//    }

    override fun isSelectionExists(): MutableState<Boolean> = isSelected

    override fun getFractalList(): List<Fractal> {
        return fractals
    }

    override fun getFractalStateList(): List<FractalState> {
        return fractalStates
    }

    override fun onClick(navController: NavHostController, id: Int) {
        viewModelScope.launch {
            navController.navigate("$SCREEN_FRACTAL_BUILDER/$id/-1")
        }
    }
}


class FractalLikedListWidgetViewModel(): FractalListWidgetViewModel() {

    lateinit var fractals: LiveData<List<Fractal>>
        private set
    var fractalStates = mutableStateListOf<FractalState>()

    val isSelected = mutableStateOf(false)

    var selectedFractalName = mutableStateOf("")

    fun setFractals(fractals: LiveData<List<Fractal>>) {
        this.fractals = fractals
    }

    override fun isSelectionExists(): MutableState<Boolean> = isSelected

    override fun getFractalList(): List<Fractal>? {
        return fractals.value
    }

    override fun getFractalStateList(): List<FractalState> {
        return fractalStates
    }

    override fun onClick(navController: NavHostController, id: Int) {
        viewModelScope.launch {
            navController.navigate("$SCREEN_FRACTAL_BUILDER/-1/$id")
        }
    }

    override fun onLongPress(navController: NavHostController, id: Int) {

        selectionId.value = id
        isSelected.value = true
        fractals.value?.let {
            selectedFractalName.value = it.find { el -> el.id == id }!!.title
        }

    }

    override fun clearSelection() {
        selectionId.value = null
        isSelected.value = false
    }
}