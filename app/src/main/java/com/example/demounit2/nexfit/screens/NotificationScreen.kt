package com.example.demounit2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.*

data class NotificationItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconBg: Color,
    val isUnread: Boolean,
    val type: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {

    val notifications = remember {
        mutableStateListOf(
            NotificationItem(1, "Workout Reminder", "Time for your Chest workout! You haven't logged today yet.", "2 mins ago", Icons.Default.FitnessCenter, PurpleAccent.copy(alpha = 0.15f), true, "today"),
            NotificationItem(2, "New PR! Bench Press", "You hit a new personal record of 105kg. Keep it up!", "1 hour ago", Icons.AutoMirrored.Filled.ShowChart, Color(0xFFD85A30).copy(alpha = 0.15f), true, "today"),
            NotificationItem(3, "Diet Plan Updated", "Your weekly meal plan is ready. Tap to view your meals.", "3 hours ago", Icons.Default.Restaurant, Color(0xFF1D9E75).copy(alpha = 0.15f), true, "today"),
            NotificationItem(4, "Weekly Summary", "You completed 4 out of 5 workouts this week. Great job!", "5 hours ago", Icons.Default.Whatshot, PurpleAccent.copy(alpha = 0.15f), true, "today"),
            NotificationItem(5, "Workout Complete", "Back workout logged successfully. 4 sets of Deadlift recorded.", "Yesterday, 6:30 PM", Icons.Default.FitnessCenter, PurpleAccent.copy(alpha = 0.15f), false, "yesterday"),
            NotificationItem(6, "Calorie Goal Reached", "You hit your 2200 kcal goal yesterday. Excellent discipline!", "Yesterday, 9:00 PM", Icons.Default.Restaurant, Color(0xFF1D9E75).copy(alpha = 0.15f), false, "yesterday")
        )
    }

    val unreadCount = notifications.count { it.isUnread }
    val todayNotifs = notifications.filter { it.type == "today" }
    val yesterdayNotifs = notifications.filter { it.type == "yesterday" }

    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Notifications", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                        if (unreadCount > 0) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PurpleAccent.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "$unreadCount new",
                                    color = PurpleAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundBlack)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        notifications.replaceAll { it.copy(isUnread = false) }
                    }) {
                        Text("Mark all as read", color = PurpleAccent, fontSize = 13.sp)
                    }
                }
            }

            if (todayNotifs.isNotEmpty()) {
                item {
                    Text(
                        text = "TODAY",
                        fontSize = 12.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )
                }
                items(todayNotifs, key = { it.id }) { notif ->
                    NotificationCard(
                        item = notif,
                        onClick = {
                            val index = notifications.indexOfFirst { it.id == notif.id }
                            if (index != -1) notifications[index] = notifications[index].copy(isUnread = false)
                        }
                    )
                }
            }

            if (yesterdayNotifs.isNotEmpty()) {
                item {
                    Text(
                        text = "YESTERDAY",
                        fontSize = 12.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                    )
                }
                items(yesterdayNotifs, key = { it.id }) { notif ->
                    NotificationCard(item = notif, onClick = {})
                }
            }

            if (notifications.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = TextGray, modifier = Modifier.size(56.dp))
                        Text("No notifications yet", color = TextGray, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (item.isUnread) PurpleAccent.copy(alpha = 0.06f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = item.iconBg,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = PurpleAccent,
                modifier = Modifier.padding(10.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = item.description, fontSize = 13.sp, color = TextGray, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.time, fontSize = 11.sp, color = Color(0xFF555555))
        }

        if (item.isUnread) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(7.dp)
                    .background(PurpleAccent, CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.size(7.dp))
        }
    }
    HorizontalDivider(color = Color(0xFF1E1E1E), thickness = 0.5.dp)
}