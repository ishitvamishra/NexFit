package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
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

@Composable
fun MuscleGroupScreen(navController: NavController) {
    val muscleGroups = listOf(
        Pair("Chest", Icons.Default.FitnessCenter),
        Pair("Back", Icons.Default.FilterHdr),
        Pair("Shoulders", Icons.Default.Accessibility),
        Pair("Biceps", Icons.Default.Add),
        Pair("Triceps", Icons.Default.HorizontalRule),
        Pair("Legs", Icons.Default.DirectionsWalk)
    )
    Column(modifier = Modifier.fillMaxSize().background(BackgroundBlack).padding(16.dp)) {
        Text(text = "Select Muscle Group", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 24.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 32.dp)) {
            items(muscleGroups) { (name, icon) ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp).clickable { navController.navigate("exercise_screen/${name.lowercase()}") },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = PurpleAccent.copy(alpha = 0.1f), modifier = Modifier.size(48.dp)) {
                                Icon(imageVector = icon, contentDescription = null, tint = PurpleAccent, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
                    }
                }
            }
        }
    }
}