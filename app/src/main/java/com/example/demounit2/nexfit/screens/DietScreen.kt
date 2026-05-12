package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demounit2.ui.theme.BackgroundBlack

@Composable
fun DietTabScreen() {
    var showDietPlan by remember { mutableStateOf(false) }

    if (showDietPlan) {
        DietPlanScreen(onBack = { showDietPlan = false })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBlack)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Diet & Nutrition",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            DietSection(onCreatePlanClick = { showDietPlan = true })
        }
    }
}