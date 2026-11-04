package com.example.loadsheddingapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import java.util.Locale

// MainActivity serves as the single Activity entry point for Jetpack Compose.
// It initializes core data dependencies (Room, DataStore, Repositories) and ViewModels,
// then observes user theme and language preferences to display the localized navigation graph.
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
            // Collect dark mode and language preferences from DataStore.
            val userPreferences by settingsViewModel.userPreferences.collectAsState()

            LocalizedApp(languageCode = userPreferences.selectedLanguage) {
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
}

// LocalizedApp dynamically updates the Composition Context with the selected language locale (English, Afrikaans, isiXhosa).
@Composable
fun LocalizedApp(
    languageCode: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val currentConfig = LocalConfiguration.current
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(currentConfig)
    config.setLocale(locale)

    val localizedContext = context.createConfigurationContext(config)

    CompositionLocalProvider(
        LocalContext provides localizedContext
    ) {
        content()
    }
}
