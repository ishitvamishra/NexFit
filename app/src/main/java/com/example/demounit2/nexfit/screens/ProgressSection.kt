package com.example.demounit2.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.demounit2.models.ProgressData
import com.example.demounit2.ui.theme.*
import kotlin.math.roundToInt

val sharedProgressDataList = listOf(
    ProgressData("Chest", "Bench Press", listOf(0.2f, 0.4f, 0.3f, 0.7f, 0.9f), "2 Months Progress", "+22%", listOf("80kg", "85kg", "82kg", "95kg", "100kg")),
    ProgressData("Shoulders", "Overhead Press", listOf(0.1f, 0.3f, 0.5f, 0.4f, 0.8f), "1 Month Progress", "+15%", listOf("40kg", "45kg", "50kg", "48kg", "60kg")),
    ProgressData("Biceps", "Curls", listOf(0.4f, 0.5f, 0.6f, 0.8f, 0.7f), "3 Months Progress", "+10%", listOf("12kg", "14kg", "16kg", "20kg", "18kg")),
    ProgressData("Legs", "Squats", listOf(0.2f, 0.3f, 0.5f, 0.6f, 0.75f), "2 Months Progress", "+12%", listOf("100kg", "110kg", "120kg", "125kg", "135kg"))
)

@Composable
fun ProgressSection(navController: NavController) {
    Column {
        Text(text = "Your Progress", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(end = 16.dp)) {
            items(sharedProgressDataList) { data ->
                ProgressCard(data, navController, modifier = Modifier.width(240.dp).height(220.dp))
            }
        }
    }
}

@Composable
fun ProgressCard(data: ProgressData, navController: NavController, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardDark), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text(text = data.muscleGroup, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = data.exercise, fontSize = 12.sp, color = TextGray)
                }
                IconButton(onClick = { navController.navigate("exercise_screen/${data.muscleGroup.lowercase()}") }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Select Exercise", tint = PurpleAccent)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            InteractiveLineChart(dataPoints = data.dataPoints, rawValues = data.rawValues, modifier = Modifier.fillMaxWidth().height(100.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = data.label, fontSize = 10.sp, color = TextGray)
                Text(text = data.percentage, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PurpleAccent)
            }
        }
    }
}

@Composable
fun InteractiveLineChart(dataPoints: List<Float>, rawValues: List<String>, modifier: Modifier = Modifier) {
    var selectedPointIndex by remember { mutableIntStateOf(-1) }
    var touchX by remember { mutableFloatStateOf(0f) }
    var touchY by remember { mutableFloatStateOf(0f) }

    Box(modifier = modifier) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier.fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { offset ->
                        val spacing = size.width / (dataPoints.size - 1)
                        val index = (offset.x / spacing).roundToInt().coerceIn(0, dataPoints.size - 1)
                        selectedPointIndex = index
                        touchX = index.toFloat() * spacing
                        touchY = size.height - (dataPoints[index] * size.height)
                        tryAwaitRelease()
                        selectedPointIndex = -1
                    })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val spacing = size.width / (dataPoints.size - 1)
                            val index = (offset.x / spacing).roundToInt().coerceIn(0, dataPoints.size - 1)
                            selectedPointIndex = index
                            touchX = index.toFloat() * spacing
                            touchY = size.height - (dataPoints[index] * size.height)
                        },
                        onDrag = { change, _ ->
                            val spacing = size.width / (dataPoints.size - 1)
                            val index = (change.position.x / spacing).roundToInt().coerceIn(0, dataPoints.size - 1)
                            selectedPointIndex = index
                            touchX = index.toFloat() * spacing
                            touchY = size.height - (dataPoints[index] * size.height)
                        },
                        onDragEnd = { selectedPointIndex = -1 },
                        onDragCancel = { selectedPointIndex = -1 }
                    )
                }
        ) {
            val width = size.width
            val height = size.height
            val spacing = width / (dataPoints.size - 1)
            val path = Path()
            val fillPath = Path()
            dataPoints.forEachIndexed { index, value ->
                val x = index * spacing
                val y = height - (value * height)
                if (index == 0) { path.moveTo(x, y); fillPath.moveTo(x, height); fillPath.lineTo(x, y) }
                else { path.lineTo(x, y); fillPath.lineTo(x, y) }
                if (index == dataPoints.size - 1) { fillPath.lineTo(x, height); fillPath.close() }
            }
            drawPath(path = fillPath, brush = Brush.verticalGradient(colors = listOf(PurpleAccent.copy(alpha = 0.3f), Color.Transparent)))
            drawPath(path = path, brush = Brush.verticalGradient(listOf(PurpleAccent, PurpleDeep)), style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
            dataPoints.forEachIndexed { index, value ->
                val x = index * spacing
                val y = height - (value * height)
                val isSelected = index == selectedPointIndex
                if (isSelected) drawCircle(color = PurpleAccent.copy(alpha = 0.4f), radius = 10.dp.toPx(), center = Offset(x, y))
                drawCircle(color = if (isSelected) Color.White else PurpleAccent, radius = if (isSelected) 5.dp.toPx() else 3.dp.toPx(), center = Offset(x, y), style = if (isSelected) Fill else Stroke(width = 2.dp.toPx()))
            }
        }
        if (selectedPointIndex != -1) {
            TooltipBox(value = rawValues[selectedPointIndex], label = "Week ${selectedPointIndex + 1}", offset = Offset(touchX, touchY))
        }
    }
}

@Composable
fun TooltipBox(value: String, label: String, offset: Offset) {
    Popup(alignment = Alignment.TopCenter, offset = IntOffset(offset.x.roundToInt(), (offset.y - 120f).roundToInt())) {
        Surface(color = CardDark, border = BorderStroke(1.dp, PurpleAccent), shape = RoundedCornerShape(8.dp), modifier = Modifier.shadow(8.dp, RoundedCornerShape(8.dp))) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = label, color = TextGray, fontSize = 10.sp)
            }
        }
    }
}