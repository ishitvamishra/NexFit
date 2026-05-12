package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.BackgroundBlack

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        item { Text(text = "Welcome back, NexFit!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White) }
        item { StatsBanner() }
        item { WorkoutSection(onClick = { navController.navigate("workouts") }) }
        item { ProgressSection(navController) }
        item { DietSection() }
    }
}