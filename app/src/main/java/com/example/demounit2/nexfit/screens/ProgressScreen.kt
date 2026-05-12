package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun ProgressScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundBlack).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text(text = "Detailed Progress", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
        }
        items(sharedProgressDataList) { data ->
            ProgressCard(data, navController, modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}