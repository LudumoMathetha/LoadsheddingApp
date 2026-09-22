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

data class SuburbSearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Suburb> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successNotification: String? = null
)

// ViewModel managing suburb search and saved suburb management.
class SuburbViewModel(
    private val suburbRepository: SuburbRepository
) : ViewModel() {

    private val _searchUiState = MutableStateFlow(SuburbSearchUiState())
    val searchUiState: StateFlow<SuburbSearchUiState> = _searchUiState.asStateFlow()

    val savedSuburbs: StateFlow<List<Suburb>> = suburbRepository.savedSuburbs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) {
        _searchUiState.value = _searchUiState.value.copy(
            searchQuery = query,
            errorMessage = null,
            successNotification = null
        )
    }

    fun searchSuburbs() {
        val query = _searchUiState.value.searchQuery.trim()
        if (query.isBlank()) {
            _searchUiState.value = _searchUiState.value.copy(
                searchResults = emptyList(),
                errorMessage = "Please enter a suburb or municipality name to search."
            )
            return
        }

        _searchUiState.value = _searchUiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            when (val result = suburbRepository.searchRemoteSuburbs(query)) {
                is NetworkResult.Success -> {
                    val results = result.data ?: emptyList()
                    _searchUiState.value = _searchUiState.value.copy(
                        isLoading = false,
                        searchResults = results,
                        errorMessage = if (results.isEmpty()) "No matching suburbs found for '$query'." else null
                    )
                }
                is NetworkResult.Error -> {
                    _searchUiState.value = _searchUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to search suburbs."
                    )
                }
                is NetworkResult.Loading -> {
                    _searchUiState.value = _searchUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun saveSuburb(suburb: Suburb) {
        viewModelScope.launch {
            if (suburbRepository.exists(suburb.suburbId)) {
                _searchUiState.value = _searchUiState.value.copy(
                    successNotification = "${suburb.name} is already saved in your list."
                )
                return@launch
            }

            suburbRepository.saveSuburb(suburb)
            _searchUiState.value = _searchUiState.value.copy(
                successNotification = "${suburb.name} was successfully added to your saved suburbs."
            )
        }
    }

    fun deleteSuburb(suburb: Suburb) {
        viewModelScope.launch {
            suburbRepository.deleteSuburb(suburb)
        }
    }

    fun toggleFavorite(suburbId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            suburbRepository.toggleFavorite(suburbId, isFavorite)
        }
    }

    fun clearNotification() {
        _searchUiState.value = _searchUiState.value.copy(successNotification = null)
    }
}
