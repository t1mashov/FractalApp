package com.example.fractalapp.saves

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.db.FractalState
import com.example.fractalapp.fractal.model.FractalColorConvert
import com.example.fractalapp.home.FractalLikedListWidgetViewModel
import com.example.fractalapp.ui.theme.FractalTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavesViewModel(
    private val repository: FractalRepository,
    val fractalListViewModel: FractalLikedListWidgetViewModel,
): ViewModel() {

    var isLoading: Boolean = false

    lateinit var fractalsLiveData: LiveData<List<Fractal>>
        private set
    val fractalStateList = mutableStateListOf<FractalState>()

    fun deleteSelectedFromLiked() {
        fractalListViewModel.selectionId.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteFractal(it)
            }
        }
        fractalListViewModel.clearSelection()
    }

    fun updateSelectedLiked() {
        fractalListViewModel.selectionId.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val title = fractalListViewModel.selectedFractalName.value
                val fractalName = title.ifEmpty { Fractal.defaultName() }
                repository.updateFractalTitle(it, fractalName)
                fractalStateList[it].title.value = fractalName
            }
        }
        fractalListViewModel.clearSelection()
    }


    init {
        getSaves()
        fractalListViewModel.fractalStates = fractalStateList

        FractalTheme.observeThemeChanges {
            viewModelScope.launch {
                for (fractal in fractalStateList) {
                    fractal.icon.value = FractalColorConvert.convert(fractal.icon.value, fractal.useColors)
                }
            }
        }
    }

    private fun getSaves() {
        viewModelScope.launch {
            isLoading = true
            fractalsLiveData = repository.getLiked()
            fractalsLiveData.observeForever {
                fractalsLiveData.value?.let { el ->
                    fractalStateList.clear()
                    fractalStateList.addAll(
                        el.map { FractalState.fromFractal(it) }
                    )
                }
            }

            fractalListViewModel.setFractals(fractalsLiveData)
            isLoading = false
        }
    }

}