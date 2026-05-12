package com.example.demounit2.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.demounit2.ui.theme.*

// ── UserSession: simple in-memory singleton to hold the logged-in user's name ──
// This avoids threading it through every composable via nav arguments after login.
object UserSession {
    var displayName: String = "NexFit Champion"
    var email: String       = "nexfit@fitness.com"
}

@Composable
fun ProfileScreen() {
    // Read from session so any update from AuthScreen is reflected here
    val name  = UserSession.displayName
    val email = UserSession.email

    // Derive initials for the avatar (up to 2 chars)
    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 32.dp)
        )

        // ── Avatar with initials ───────────────────────────────────────────────
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = PurpleAccent.copy(alpha = 0.15f),
            border = BorderStroke(2.dp, PurpleAccent)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (initials.isNotBlank()) {
                    Text(
                        text = initials,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Name & email ───────────────────────────────────────────────────────
        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = email,
            fontSize = 16.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ── Options card ───────────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileOption(Icons.Default.Settings, "Account Settings")
                HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))
                ProfileOption(Icons.Default.Notifications, "Notification Settings")
                HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))
                ProfileOption(Icons.AutoMirrored.Filled.Help, "Help & Support")
            }
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PurpleAccent, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
    }
}