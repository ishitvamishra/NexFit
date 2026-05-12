package com.example.demounit2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSelectionScreen(muscleGroup: String, navController: NavController) {
    val exercises = when (muscleGroup) {
        "chest" -> listOf("Bench Press", "Incline Press", "Chest Fly", "Push Ups")
        "biceps" -> listOf("Barbell Curl", "Hammer Curl", "Preacher Curl", "Concentration Curl")
        "legs" -> listOf("Squat", "Leg Press", "Leg Extension", "Lunge")
        "shoulders" -> listOf("Overhead Press", "Lateral Raise", "Front Raise", "Face Pull")
        "back" -> listOf("Lat Pulldown", "Deadlift", "Bent Over Row", "Pull Up")
        "triceps" -> listOf("Tricep Pushdown", "Skull Crusher", "Dip", "Overhead Extension")
        else -> listOf("Unknown Exercise")
    }
    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Exercise", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundBlack)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text(text = "Exercises for ${muscleGroup.replaceFirstChar { it.uppercase() }}", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp)) }
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("workout_logger/${exercise}") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = exercise, color = Color.White, fontWeight = FontWeight.Medium)
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = PurpleAccent)
                    }
                }
            }
        }
    }
}