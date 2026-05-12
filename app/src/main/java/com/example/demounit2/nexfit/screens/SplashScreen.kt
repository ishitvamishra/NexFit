package com.example.demounit2.nexfit.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.PurpleAccent
import com.example.demounit2.ui.theme.BackgroundBlack
import kotlinx.coroutines.delay

// ─── Route constant ───────────────────────────────────────────────────────────
// Add "splash" as the startDestination in your NavGraph and navigate from here
// to "home" (or whatever your main route is) after the delay.
const val SPLASH_ROUTE = "splash"

@Composable
fun SplashScreen(navController: NavController) {

    // ── 1. Animate logo scale: pop in from 0.4 → 1.05 → 1.0 ──────────────────
    val logoScale = remember { Animatable(0.4f) }
    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(durationMillis = 600, easing = EaseOutBack)
        )
        logoScale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 150, easing = LinearEasing)
        )
    }

    // ── 2. Fade in the tagline after the logo settles ─────────────────────────
    val taglineAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(500)
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = LinearEasing)
        )
    }

    // ── 3. Pulsing glow ring behind the logo icon ─────────────────────────────
    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // ── 4. Navigate to home after 2.5 s ──────────────────────────────────────
    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("auth") {   // ← was "home"
            popUpTo("splash") { inclusive = true }
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BackgroundBlack,
                        Color(0xFF0D0D1A),   // very dark indigo at bottom
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Soft radial glow behind logo
        Box(
            modifier = Modifier
                .size(260.dp)
                .alpha(glowAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(PurpleAccent.copy(alpha = 0.55f), Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo mark ────────────────────────────────────────────────────
            // Drawn entirely in Compose — no image asset needed.
            // A bold "N" lettermark inside a hexagonal-ish rounded square.
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .size(110.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PurpleAccent, Color(0xFF7B2FFF))
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "N",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-2).sp
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── App name ─────────────────────────────────────────────────────
            Text(
                text = "NEXFIT",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 8.sp,
                modifier = Modifier.scale(logoScale.value)
            )

            Spacer(Modifier.height(10.dp))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text = "Train. Track. Transform.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PurpleAccent.copy(alpha = 0.85f),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }

        // ── Version tag at bottom ─────────────────────────────────────────────
        Text(
            text = "v1.0",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.25f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(taglineAlpha.value)
        )
    }
}