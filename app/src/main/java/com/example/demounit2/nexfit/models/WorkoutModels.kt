package com.example.demounit2.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ProgressData(
    val muscleGroup: String,
    val exercise: String,
    val dataPoints: List<Float>,
    val label: String,
    val percentage: String,
    val rawValues: List<String>
)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

data class WorkoutSet(
    var reps: String = "",
    var weight: String = ""
)