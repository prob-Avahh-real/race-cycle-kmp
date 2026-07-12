// App.kt — race-cycle entry point
package com.racecycle

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.racecycle.ui.screens.DashboardScreen
import com.racecycle.model.RaceGoal

private val RaceColors = darkColorScheme(
    primary = Color(0xFFFB8C00),
    secondary = Color(0xFF1B5E20),
    tertiary = Color(0xFFFF7043),
    surface = Color(0xFF121212),
    background = Color(0xFF0A0A0A)
)

@Composable
fun App() {
    MaterialTheme(colorScheme = RaceColors) {
        // TODO: load from persistence
        val goal = remember {
            // val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            // Default: 12-week half marathon
            RaceGoal("半马训练", 0L /* replace with real date */, "21.0975 km", 12)
        }
        val checkins = remember { mutableStateListOf<com.racecycle.model.WeeklyCheckin>() }
        var showCheckin by remember { mutableStateOf(false) }

        if (showCheckin) {
            // TODO: CheckinScreen
        } else {
            DashboardScreen(
                goal = goal,
                checkins = checkins,
                onCheckin = { showCheckin = true },
                onReset = { /* reset flow */ }
            )
        }
    }
}
