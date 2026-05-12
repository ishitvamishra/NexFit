package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.demounit2.ui.theme.*

// ─── Data models ────────────────────────────────────────────────────────────

data class FoodItem(
    val name: String,
    val calories: Int,
    val protein: String,
    val carbs: String,
    val fat: String
)

data class HealthTip(
    val emoji: String,
    val title: String,
    val description: String
)

// ─── Healthy food tips per meal ──────────────────────────────────────────────

val breakfastTips = listOf(
    HealthTip("🥣", "Oats & Berries", "Rich in fibre & antioxidants. Keeps you full till noon and regulates blood sugar."),
    HealthTip("🥚", "Eggs", "High-quality protein with all essential amino acids. Great for muscle repair."),
    HealthTip("🍌", "Banana", "Quick energy boost with natural sugars and potassium for muscle function."),
    HealthTip("🥛", "Greek Yogurt", "Probiotics + protein combo. Supports gut health and satiety."),
    HealthTip("🌰", "Nuts & Seeds", "Healthy fats (omega-3) that support brain health and keep hunger at bay.")
)

val lunchTips = listOf(
    HealthTip("🥗", "Salad with Greens", "Spinach & kale are iron-rich. Add chickpeas for plant-based protein."),
    HealthTip("🍗", "Grilled Chicken", "Lean protein with low fat. Ideal for muscle building and satiety."),
    HealthTip("🍚", "Brown Rice", "Complex carbs for sustained energy. Better than white rice for glycemic control."),
    HealthTip("🫘", "Lentils / Dal", "High in protein & fibre. A staple Indian superfood for gut and heart health."),
    HealthTip("🥕", "Colourful Veggies", "Mix red, orange & green vegetables for a wide range of vitamins & minerals.")
)

val dinnerTips = listOf(
    HealthTip("🐟", "Fish (Salmon/Rohu)", "Omega-3 fatty acids reduce inflammation and support heart health overnight."),
    HealthTip("🥦", "Steamed Vegetables", "Light on the stomach at night. Broccoli & cauliflower are high in vitamin C."),
    HealthTip("🍲", "Vegetable Soup", "Hydrating and easy to digest. A light dinner helps better sleep quality."),
    HealthTip("🫓", "Whole Wheat Roti", "Complex carbs + fibre. Prefer over white flour for better digestion at night."),
    HealthTip("🧄", "Garlic & Turmeric", "Anti-inflammatory spices. Add to your cooking for immune and gut benefits.")
)

// ─── Main screen ─────────────────────────────────────────────────────────────

@Composable
fun DietPlanScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Breakfast", "Lunch", "Dinner")

    val breakfastItems = remember { mutableStateListOf<FoodItem>() }
    val lunchItems     = remember { mutableStateListOf<FoodItem>() }
    val dinnerItems    = remember { mutableStateListOf<FoodItem>() }

    var showDialog by remember { mutableStateOf(false) }

    val currentItems = when (selectedTab) {
        0 -> breakfastItems
        1 -> lunchItems
        else -> dinnerItems
    }

    val tips = when (selectedTab) {
        0 -> breakfastTips
        1 -> lunchTips
        else -> dinnerTips
    }

    val totalCalories = currentItems.sumOf { it.calories }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
    ) {
        // ── Top bar ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Create Diet Plan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
            // Calorie badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(PurpleAccent.copy(alpha = 0.2f))
                    .border(1.dp, PurpleAccent, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$totalCalories kcal",
                    fontSize = 12.sp,
                    color = PurpleAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // ── Tabs ──────────────────────────────────────────────────────────────
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceDark,
            contentColor = PurpleAccent,
            modifier = Modifier.padding(horizontal = 16.dp).clip(RoundedCornerShape(12.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) PurpleAccent else TextGray
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Content ───────────────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Section: Added foods
            if (currentItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Your ${tabs[selectedTab]} Plan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(currentItems) { food ->
                    FoodItemCard(food = food, onDelete = { currentItems.remove(food) })
                }
            }

            // Add button
            item {
                OutlinedButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleAccent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PurpleAccent)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add ${tabs[selectedTab]} Item",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Section: Healthy tips
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 Healthy ${tabs[selectedTab]} Ideas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tap any item to add it to your plan",
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            items(tips) { tip ->
                HealthTipCard(
                    tip = tip,
                    onAdd = {
                        // Quick-add with estimated values
                        currentItems.add(
                            FoodItem(
                                name = tip.title,
                                calories = estimatedCalories(tip.title),
                                protein = estimatedProtein(tip.title),
                                carbs = estimatedCarbs(tip.title),
                                fat = estimatedFat(tip.title)
                            )
                        )
                    }
                )
            }
        }
    }

    // ── Add food dialog ───────────────────────────────────────────────────────
    if (showDialog) {
        AddFoodDialog(
            mealName = tabs[selectedTab],
            onDismiss = { showDialog = false },
            onAdd = { item ->
                currentItems.add(item)
                showDialog = false
            }
        )
    }
}

// ─── Food item card ───────────────────────────────────────────────────────────

@Composable
fun FoodItemCard(food: FoodItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Restaurant,
                contentDescription = null,
                tint = PurpleAccent,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(food.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroBadge("🔥 ${food.calories}", "kcal")
                    MacroBadge("💪 ${food.protein}", "P")
                    MacroBadge("🌾 ${food.carbs}", "C")
                    MacroBadge("🧈 ${food.fat}", "F")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFFF6B6B))
            }
        }
    }
}

@Composable
fun MacroBadge(value: String, label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$value $label",
            fontSize = 10.sp,
            color = TextGray
        )
    }
}

// ─── Health tip card ──────────────────────────────────────────────────────────

@Composable
fun HealthTipCard(tip: HealthTip, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tip.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tip.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = tip.description,
                    fontSize = 11.sp,
                    color = TextGray,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PurpleAccent.copy(alpha = 0.15f))
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = PurpleAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Add food dialog ──────────────────────────────────────────────────────────

@Composable
fun AddFoodDialog(
    mealName: String,
    onDismiss: () -> Unit,
    onAdd: (FoodItem) -> Unit
) {
    var name     by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein  by remember { mutableStateOf("") }
    var carbs    by remember { mutableStateOf("") }
    var fat      by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add $mealName Item",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                DietTextField(value = name, label = "Food Name (e.g. Oats, Egg)", onValueChange = { name = it })
                Spacer(modifier = Modifier.height(10.dp))
                DietTextField(value = calories, label = "Calories (kcal)", onValueChange = { calories = it }, isNumber = true)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DietTextField(value = protein, label = "Protein (g)", onValueChange = { protein = it }, isNumber = true, modifier = Modifier.weight(1f))
                    DietTextField(value = carbs,   label = "Carbs (g)",   onValueChange = { carbs = it },   isNumber = true, modifier = Modifier.weight(1f))
                    DietTextField(value = fat,     label = "Fat (g)",     onValueChange = { fat = it },     isNumber = true, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGray),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TextGray)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onAdd(
                                    FoodItem(
                                        name     = name.trim(),
                                        calories = calories.toIntOrNull() ?: 0,
                                        protein  = if (protein.isNotBlank()) "${protein}g" else "0g",
                                        carbs    = if (carbs.isNotBlank())   "${carbs}g"   else "0g",
                                        fat      = if (fat.isNotBlank())     "${fat}g"     else "0g"
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                    ) {
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DietTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp) },
        singleLine = true,
        keyboardOptions = if (isNumber)
            KeyboardOptions(keyboardType = KeyboardType.Number)
        else
            KeyboardOptions.Default,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor    = Color.White,
            unfocusedTextColor  = Color.White,
            focusedBorderColor  = PurpleAccent,
            unfocusedBorderColor = TextGray,
            focusedLabelColor   = PurpleAccent,
            unfocusedLabelColor = TextGray,
            cursorColor         = PurpleAccent
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

// ─── Quick estimate helpers (approx values) ───────────────────────────────────

private fun estimatedCalories(name: String) = when {
    name.contains("Oat", true)     -> 150
    name.contains("Egg", true)     -> 155
    name.contains("Banana", true)  -> 90
    name.contains("Yogurt", true)  -> 100
    name.contains("Nuts", true)    -> 170
    name.contains("Chicken", true) -> 165
    name.contains("Rice", true)    -> 215
    name.contains("Lentil", true)  -> 230
    name.contains("Salad", true)   -> 80
    name.contains("Fish", true)    -> 180
    name.contains("Soup", true)    -> 70
    name.contains("Roti", true)    -> 120
    else                           -> 100
}

private fun estimatedProtein(name: String) = when {
    name.contains("Egg", true)     -> "13g"
    name.contains("Chicken", true) -> "31g"
    name.contains("Fish", true)    -> "25g"
    name.contains("Yogurt", true)  -> "10g"
    name.contains("Lentil", true)  -> "18g"
    else                           -> "5g"
}

private fun estimatedCarbs(name: String) = when {
    name.contains("Oat", true)     -> "27g"
    name.contains("Banana", true)  -> "23g"
    name.contains("Rice", true)    -> "45g"
    name.contains("Roti", true)    -> "22g"
    name.contains("Lentil", true)  -> "40g"
    else                           -> "10g"
}

private fun estimatedFat(name: String) = when {
    name.contains("Nuts", true)    -> "15g"
    name.contains("Egg", true)     -> "11g"
    name.contains("Fish", true)    -> "8g"
    name.contains("Chicken", true) -> "3.6g"
    else                           -> "2g"
}