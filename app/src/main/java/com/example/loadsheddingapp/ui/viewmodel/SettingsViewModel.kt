package com.example.loadsheddingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadsheddingapp.data.preferences.UserPreferencesManager
import com.example.loadsheddingapp.domain.model.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel managing app configuration preferences stored in DataStore.
class SettingsViewModel(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences> = preferencesManager.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDarkMode(enabled)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            preferencesManager.updateLanguage(languageCode)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateNotificationsEnabled(enabled)
        }
    }

    fun setNotificationLeadTime(leadTimeMinutes: Int) {
        viewModelScope.launch {
            preferencesManager.updateNotificationLeadTime(leadTimeMinutes)
        }
    }
}
