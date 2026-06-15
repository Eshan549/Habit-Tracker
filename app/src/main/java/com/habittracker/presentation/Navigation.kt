package com.habittracker.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.habittracker.presentation.ui.addhabit.AddHabitScreen
import com.habittracker.presentation.ui.home.HomeScreen
import com.habittracker.presentation.ui.settings.SettingsScreen
import com.habittracker.presentation.ui.statistics.StatisticsScreen
import com.habittracker.util.ThemeMode

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddHabit : Screen("add_habit?habitId={habitId}") {
        fun createRoute(habitId: Long? = null) =
            if (habitId != null) "add_habit?habitId=$habitId" else "add_habit?habitId=-1"
    }
    object Statistics : Screen("statistics/{habitId}") {
        fun createRoute(habitId: Long) = "statistics/$habitId"
    }
    object Settings : Screen("settings")
}

@Composable
fun HabitNavHost(
    navController: NavHostController,
    themeMode: ThemeMode,
    onSetThemeMode: (ThemeMode) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddHabit = { navController.navigate(Screen.AddHabit.createRoute()) },
                onEditHabit = { id -> navController.navigate(Screen.AddHabit.createRoute(id)) },
                onViewStats = { id -> navController.navigate(Screen.Statistics.createRoute(id)) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(
            route = Screen.AddHabit.route,
            arguments = listOf(navArgument("habitId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStack ->
            val habitId = backStack.arguments?.getLong("habitId") ?: -1L
            AddHabitScreen(
                habitId = if (habitId == -1L) null else habitId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Statistics.route,
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) { backStack ->
            val habitId = backStack.arguments!!.getLong("habitId")
            StatisticsScreen(habitId = habitId, onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                themeMode = themeMode,
                onSetThemeMode = onSetThemeMode,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
