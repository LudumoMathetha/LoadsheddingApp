package com.example.loadsheddingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel for Dashboard screen managing national stage banner and saved suburbs overview.
class DashboardViewModel(
    private val suburbRepository: SuburbRepository
) : ViewModel() {

    private val _nationalStage = MutableStateFlow(2) // Default stage 2
    val nationalStage: StateFlow<Int> = _nationalStage.asStateFlow()

    val savedSuburbs: StateFlow<List<Suburb>> = suburbRepository.savedSuburbs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    init {
        loadNationalStage()
    }

    fun loadNationalStage() {
        viewModelScope.launch {
            when (val result = suburbRepository.getNationalStage()) {
                is NetworkResult.Success -> {
                    _nationalStage.value = result.data ?: 2
                }
                else -> {
                    _nationalStage.value = 2
                }
            }
        }
    }

    fun toggleFavorite(suburbId: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            suburbRepository.toggleFavorite(suburbId, !currentFavorite)
        }
    }

    fun removeSuburb(suburb: Suburb) {
        viewModelScope.launch {
            suburbRepository.deleteSuburb(suburb)
        }
    }
}
