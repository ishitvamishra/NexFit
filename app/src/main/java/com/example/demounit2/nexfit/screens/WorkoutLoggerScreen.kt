package com.example.demounit2.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.*

// ✅ FIX: WorkoutSet fields must use mutableStateOf so Compose can observe changes.
//    A plain data class with var fields is invisible to the Compose snapshot system —
//    typing in a TextField updates the field but never triggers recomposition.
class WorkoutSet {
    var reps: String by mutableStateOf("")
    var weight: String by mutableStateOf("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutLoggerScreen(exerciseName: String, navController: NavController) {
    // ✅ FIX: mutableStateListOf ensures the list itself (add/remove) triggers recomposition.
    //    Combined with the mutableStateOf fields inside WorkoutSet, every change
    //    (adding a set, deleting a set, typing weight/reps) now recomposes correctly.
    val sets = remember { mutableStateListOf(WorkoutSet()) }
    val context = LocalContext.current

    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            exerciseName,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Text("Log your sets", color = TextGray, fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundBlack
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BackgroundBlack,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Workout Saved!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack("workouts", inclusive = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save Workout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = sets,
                // ✅ FIX: Provide a stable key so LazyColumn correctly tracks each set
                //    when items are added or removed mid-list.
                key = { _, set -> set.hashCode() }
            ) { index, set ->
                SetRow(
                    setNumber = index + 1,
                    reps = set.reps,
                    weight = set.weight,
                    onRepsChange = { set.reps = it },
                    onWeightChange = { set.weight = it },
                    onDelete = {
                        if (sets.size > 1) sets.removeAt(index)
                    }
                )
            }

            item {
                OutlinedButton(
                    onClick = { sets.add(WorkoutSet()) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleAccent),
                    border = BorderStroke(1.dp, PurpleAccent)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Set")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetRow(
    setNumber: Int,
    reps: String,
    weight: String,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "SET $setNumber",
                fontWeight = FontWeight.Bold,
                color = PurpleAccent,
                modifier = Modifier.width(50.dp)
            )
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                modifier = Modifier.weight(1f),
                label = { Text("Weight (kg)", fontSize = 10.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = PurpleAccent,
                    unfocusedIndicatorColor = TextGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            OutlinedTextField(
                value = reps,
                onValueChange = onRepsChange,
                modifier = Modifier.weight(1f),
                label = { Text("Reps", fontSize = 10.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = PurpleAccent,
                    unfocusedIndicatorColor = TextGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}