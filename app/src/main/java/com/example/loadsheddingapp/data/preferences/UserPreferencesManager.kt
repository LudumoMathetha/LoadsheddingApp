package com.example.loadsheddingapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.loadsheddingapp.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

// UserPreferencesManager uses Jetpack DataStore Preferences for light app settings.
// DataStore replaces older SharedPreferences with reactive Flow streams and coroutine support.
class UserPreferencesManager(private val context: Context) {

    companion object {
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        val KEY_LANGUAGE = stringPreferencesKey("selected_language")
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val KEY_NOTIFICATION_LEAD_TIME = intPreferencesKey("notification_lead_time")
    }

    // Flow mapping stored DataStore preferences into a clean UserPreferences domain model.
    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val darkMode = preferences[KEY_DARK_MODE] ?: false
        val language = preferences[KEY_LANGUAGE] ?: "en"
        val notificationsEnabled = preferences[KEY_NOTIFICATIONS_ENABLED] ?: true
        val leadTimeMinutes = preferences[KEY_NOTIFICATION_LEAD_TIME] ?: 15

        UserPreferences(
            darkMode = darkMode,
            selectedLanguage = language,
            notificationsEnabled = notificationsEnabled,
            notificationLeadTimeMinutes = leadTimeMinutes
        )
    }

    // Updates dark mode preference in DataStore.
    suspend fun updateDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = enabled
        }
    }

    // Updates selected application language code.
    suspend fun updateLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = languageCode
        }
    }

    // Updates notification alert preference.
    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    // Updates notification lead time choice in minutes.
    suspend fun updateNotificationLeadTime(leadTimeMinutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATION_LEAD_TIME] = leadTimeMinutes
        }
    }
}
