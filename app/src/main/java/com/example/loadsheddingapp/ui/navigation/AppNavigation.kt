package com.example.loadsheddingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loadsheddingapp.ui.screens.auth.LoginScreen
import com.example.loadsheddingapp.ui.screens.auth.RegisterScreen
import com.example.loadsheddingapp.ui.screens.dashboard.DashboardScreen
import com.example.loadsheddingapp.ui.screens.schedule.ScheduleDetailScreen
import com.example.loadsheddingapp.ui.screens.settings.SettingsScreen
import com.example.loadsheddingapp.ui.screens.suburbs.SavedSuburbsScreen
import com.example.loadsheddingapp.ui.screens.suburbs.SearchSuburbScreen
import com.example.loadsheddingapp.ui.viewmodel.AuthViewModel
import com.example.loadsheddingapp.ui.viewmodel.DashboardViewModel
import com.example.loadsheddingapp.ui.viewmodel.ScheduleViewModel
import com.example.loadsheddingapp.ui.viewmodel.SettingsViewModel
import com.example.loadsheddingapp.ui.viewmodel.SuburbViewModel

// AppNavigation composable handling route destinations, argument passing, and screen switching.
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    suburbViewModel: SuburbViewModel,
    scheduleViewModel: ScheduleViewModel,
    settingsViewModel: SettingsViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val startDestination = if (currentUser != null) Screen.Dashboard.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                dashboardViewModel = dashboardViewModel,
                authViewModel = authViewModel,
                onNavigateToSearch = {
                    navController.navigate(Screen.SearchSuburb.route)
                },
                onNavigateToSavedSuburbs = {
                    navController.navigate(Screen.SavedSuburbs.route)
                },
                onNavigateToSchedule = { suburbId ->
                    navController.navigate(Screen.ScheduleDetail.createRoute(suburbId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SearchSuburb.route) {
            SearchSuburbScreen(
                suburbViewModel = suburbViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSchedule = { suburbId ->
                    navController.navigate(Screen.ScheduleDetail.createRoute(suburbId))
                }
            )
        }

        composable(Screen.SavedSuburbs.route) {
            SavedSuburbsScreen(
                suburbViewModel = suburbViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSearch = { navController.navigate(Screen.SearchSuburb.route) },
                onNavigateToSchedule = { suburbId ->
                    navController.navigate(Screen.ScheduleDetail.createRoute(suburbId))
                }
            )
        }

        composable(
            route = Screen.ScheduleDetail.route,
            arguments = listOf(navArgument("suburbId") { type = NavType.StringType })
        ) { backStackEntry ->
            val suburbId = backStackEntry.arguments?.getString("suburbId") ?: ""
            ScheduleDetailScreen(
                suburbId = suburbId,
                scheduleViewModel = scheduleViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
