package com.example.loadsheddingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.loadsheddingapp.data.local.AppDatabase
import com.example.loadsheddingapp.data.preferences.UserPreferencesManager
import com.example.loadsheddingapp.data.repository.AuthRepository
import com.example.loadsheddingapp.data.repository.ScheduleRepository
import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.ui.navigation.AppNavigation
import com.example.loadsheddingapp.ui.theme.LoadsheddingAppTheme
import com.example.loadsheddingapp.ui.viewmodel.AuthViewModel
import com.example.loadsheddingapp.ui.viewmodel.DashboardViewModel
import com.example.loadsheddingapp.ui.viewmodel.ScheduleViewModel
import com.example.loadsheddingapp.ui.viewmodel.SettingsViewModel
import com.example.loadsheddingapp.ui.viewmodel.SuburbViewModel
import com.example.loadsheddingapp.ui.viewmodel.ViewModelFactory

// MainActivity serves as the single Activity entry point for Jetpack Compose.
// It initializes core data dependencies (Room, DataStore, Repositories) and ViewModels,
// then observes user theme preferences to display the main navigation graph.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize local Room Database and DataStore preferences manager instances.
        val database = AppDatabase.getDatabase(applicationContext)
        val preferencesManager = UserPreferencesManager(applicationContext)

        // Instantiate repository layer instances so ViewModels do not access DAOs or Retrofit directly.
        val authRepository = AuthRepository(database.userDao())
        val suburbRepository = SuburbRepository(database.suburbDao())
        val scheduleRepository = ScheduleRepository(database.scheduleDao())

        // Use a single ViewModelFactory to pass required repository parameters to ViewModels.
        val factory = ViewModelFactory(
            authRepository = authRepository,
            suburbRepository = suburbRepository,
            scheduleRepository = scheduleRepository,
            preferencesManager = preferencesManager
        )

        // Instantiate ViewModels bound to the Activity lifecycle.
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        val dashboardViewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
        val suburbViewModel = ViewModelProvider(this, factory)[SuburbViewModel::class.java]
        val scheduleViewModel = ViewModelProvider(this, factory)[ScheduleViewModel::class.java]
        val settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        setContent {
            // Collect dark mode preference state from DataStore to apply light or dark theme dynamically.
            val userPreferences by settingsViewModel.userPreferences.collectAsState()

            LoadsheddingAppTheme(darkTheme = userPreferences.darkMode) {
                AppNavigation(
                    authViewModel = authViewModel,
                    dashboardViewModel = dashboardViewModel,
                    suburbViewModel = suburbViewModel,
                    scheduleViewModel = scheduleViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
