package com.example.loadsheddingapp.domain.model

// Domain model holding simple user settings and preferences.
data class UserPreferences(
    val darkMode: Boolean = false,
    val selectedLanguage: String = "en",
    val notificationsEnabled: Boolean = true,
    val notificationLeadTimeMinutes: Int = 15
)
