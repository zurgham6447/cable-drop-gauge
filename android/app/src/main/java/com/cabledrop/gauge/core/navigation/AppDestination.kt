package com.cabledrop.gauge.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Game : Screen
    data class GameOver(val runId: String) : Screen
    data object Diagram : Screen
    data object Settings : Screen
    data object Safety : Screen
}
