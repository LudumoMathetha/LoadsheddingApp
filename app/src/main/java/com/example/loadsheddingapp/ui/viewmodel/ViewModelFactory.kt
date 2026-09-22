package com.example.loadsheddingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loadsheddingapp.data.preferences.UserPreferencesManager
import com.example.loadsheddingapp.data.repository.AuthRepository
import com.example.loadsheddingapp.data.repository.ScheduleRepository
import com.example.loadsheddingapp.data.repository.SuburbRepository

// Custom ViewModelProvider.Factory for instantiating ViewModels with repository dependencies.
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val suburbRepository: SuburbRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(suburbRepository) as T
            }
            modelClass.isAssignableFrom(SuburbViewModel::class.java) -> {
                SuburbViewModel(suburbRepository) as T
            }
            modelClass.isAssignableFrom(ScheduleViewModel::class.java) -> {
                ScheduleViewModel(scheduleRepository, suburbRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferencesManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
