package com.example.fractalapp.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.fractalapp.*
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(

    private val repository: FractalRepository,
    val fractalListViewModel: FractalSamplesListWidgetViewModel,

    val samples: SnapshotStateList<Fractal> = mutableStateListOf(),
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

): ViewModel() {

    init {
        loadSamples()
        fractalListViewModel.setFractals(samples)
    }

    private fun loadSamples() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            samples.clear()
            samples.addAll(
                repository.getSamples()
            )
            isLoading.value = false
        }
    }

    fun redirectToFractalBuilder(navController: NavController) {
        viewModelScope.launch {
            navController.navigate("$SCREEN_FRACTAL_BUILDER/-1/-1")
        }
    }
    fun redirectToSaves(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SCREEN_SAVED)
        }
    }


}