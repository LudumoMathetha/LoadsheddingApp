package com.example.loadsheddingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadsheddingapp.data.repository.ScheduleRepository
import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.domain.model.Schedule
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ScheduleDetailUiState(
    val suburb: Suburb? = null,
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCachedData: Boolean = false
)

// ViewModel managing schedule fetching, cached state, and date filtering.
class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val suburbRepository: SuburbRepository
) : ViewModel() {

    private val _scheduleUiState = MutableStateFlow(ScheduleDetailUiState())
    val scheduleUiState: StateFlow<ScheduleDetailUiState> = _scheduleUiState.asStateFlow()

    fun loadScheduleForSuburb(suburbId: String) {
        _scheduleUiState.value = _scheduleUiState.value.copy(isLoading = true, errorMessage = null, isCachedData = false)

        viewModelScope.launch {
            val suburb = suburbRepository.getSuburbById(suburbId).firstOrNull()

            when (val result = scheduleRepository.fetchAndCacheSchedule(suburbId)) {
                is NetworkResult.Success -> {
                    val schedules = result.data ?: emptyList()
                    _scheduleUiState.value = _scheduleUiState.value.copy(
                        suburb = suburb,
                        schedules = schedules,
                        isLoading = false,
                        isCachedData = false,
                        errorMessage = if (schedules.isEmpty()) "No schedule slots available for this suburb." else null
                    )
                }
                is NetworkResult.Error -> {
                    val cachedList = result.data ?: scheduleRepository.getSchedulesForSuburb(suburbId).firstOrNull() ?: emptyList()
                    _scheduleUiState.value = _scheduleUiState.value.copy(
                        suburb = suburb,
                        schedules = cachedList,
                        isLoading = false,
                        isCachedData = true,
                        errorMessage = if (cachedList.isEmpty()) result.message ?: "Unable to fetch schedule." else null
                    )
                }
                is NetworkResult.Loading -> {
                    _scheduleUiState.value = _scheduleUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}
