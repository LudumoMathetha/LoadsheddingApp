package com.example.loadsheddingapp.ui.navigation

// Sealed class representing type-safe navigation routes across the application.
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object SearchSuburb : Screen("search_suburb")
    object SavedSuburbs : Screen("saved_suburbs")
    object ScheduleDetail : Screen("schedule_detail/{suburbId}") {
        fun createRoute(suburbId: String): String = "schedule_detail/$suburbId"
    }
    object Settings : Screen("settings")
}
