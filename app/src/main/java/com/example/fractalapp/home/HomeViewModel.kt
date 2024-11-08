package com.example.fractalapp.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.fractalapp.*
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.db.FractalState
import com.example.fractalapp.fractal.model.FractalColorConvert
import com.example.fractalapp.ui.theme.FractalTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(

    private val repository: FractalRepository,
    val fractalListViewModel: FractalSamplesListWidgetViewModel,

    val samples: SnapshotStateList<Fractal> = mutableStateListOf(),
    val sampleStates: SnapshotStateList<FractalState> = mutableStateListOf(),
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

): ViewModel() {

    init {
        loadSamples()
        fractalListViewModel.setFractals(samples)
        fractalListViewModel.fractalStates = sampleStates
    }

    private fun loadSamples() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            val repoSamples = repository.getSamples()
            samples.clear()
            samples.addAll(
                repoSamples
            )

            sampleStates.clear()
            sampleStates.addAll(
                repoSamples.map { FractalState.fromFractal(it) }
            )
            isLoading.value = false
        }
        FractalTheme.observeThemeChanges {
            viewModelScope.launch {
                for (sample in sampleStates) {
                    sample.icon.value = FractalColorConvert.convert(sample.icon.value, sample.useColors)
                }
            }
        }
    }

    fun redirectToFractalBuilder(navController: NavController) {
        viewModelScope.launch {
            navController.navigate("$SCREEN_FRACTAL_BUILDER/-1/-1")
        }
    }
    fun redirectToRulesText(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SCREEN_ABOUT_L_SYSTEM)
        }
    }
    fun redirectToSaves(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SCREEN_SAVED)
        }
    }


}