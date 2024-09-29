package com.example.fractalapp.saves

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.home.FractalLikedListWidgetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavesViewModel(
    private val repository: FractalRepository,
    val fractalListViewModel: FractalLikedListWidgetViewModel,

): ViewModel() {

    var isLoading: Boolean = false

    lateinit var fractalsLiveData: LiveData<List<Fractal>>
        private set

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
            }
        }
        fractalListViewModel.clearSelection()
    }


    init {
        getSaves()
    }

    private fun getSaves() {
        viewModelScope.launch {
            isLoading = true
            fractalsLiveData = repository.getLiked()
            fractalListViewModel.setFractals(fractalsLiveData)
            isLoading = false
        }
    }

}