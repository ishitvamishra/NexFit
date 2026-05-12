package com.example.demounit2.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.demounit2.models.BottomNavItem
import com.example.demounit2.ui.theme.BackgroundBlack
import com.example.demounit2.ui.theme.PurpleAccent
import com.example.demounit2.ui.theme.TextGray

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("workouts", Icons.Default.FitnessCenter, "Workouts"),
        BottomNavItem("progress", Icons.AutoMirrored.Filled.ShowChart, "Progress"),
        BottomNavItem("diet", Icons.Default.Restaurant, "Diet"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    NavigationBar(
        containerColor = BackgroundBlack,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route ||
                    (item.route == "workouts" && (currentRoute?.startsWith("exercise_screen") == true || currentRoute?.startsWith("workout_logger") == true))

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = false
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurpleAccent,
                    unselectedIconColor = TextGray,
                    selectedTextColor = PurpleAccent,
                    unselectedTextColor = TextGray,
                    indicatorColor = PurpleAccent.copy(alpha = 0.1f)
                )
            )
        }
    }
}