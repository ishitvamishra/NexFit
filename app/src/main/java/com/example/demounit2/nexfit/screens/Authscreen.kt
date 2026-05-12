package com.example.demounit2.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demounit2.ui.theme.*

@Composable
fun AuthScreen(navController: NavController) {

    // 0 = Sign In, 1 = Sign Up
    var selectedTab     by remember { mutableIntStateOf(0) }
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ── Helper: save to UserSession and navigate to home ─────────────────────
    fun loginWith(displayName: String, userEmail: String) {
        UserSession.displayName = displayName.ifBlank { "Champion" }
        UserSession.email       = userEmail.ifBlank { "nexfit@fitness.com" }
        navController.navigate("home") {
            popUpTo("auth") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0D1A), BackgroundBlack)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Logo ──────────────────────────────────────────────────────────
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                color = PurpleAccent
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("N", fontSize = 44.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "NEXFIT",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                color = Color.White
            )
            Text(
                text = "Your fitness journey starts here",
                fontSize = 13.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // ── Tab switcher ──────────────────────────────────────────────────
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    listOf("Sign In", "Sign Up").forEachIndexed { index, label ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (selectedTab == index) PurpleAccent else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedTab = index }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = if (selectedTab == index) Color.White else TextGray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Form fields ───────────────────────────────────────────────────
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState)
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    else
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                },
                label = "tab_transition"
            ) { tab ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (tab == 1) {
                        AuthTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person
                        )
                    }

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible }
                    )

                    if (tab == 0) {
                        Text(
                            text = "Forgot Password?",
                            color = PurpleAccent,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // ── CTA Button ────────────────────────────────────────────
                    Button(
                        onClick = {
                            val displayName = if (tab == 1 && name.isNotBlank()) name
                            else email.substringBefore("@")
                                .replaceFirstChar { it.uppercaseChar() }
                            loginWith(displayName, email)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                    ) {
                        Text(
                            text = if (tab == 0) "Sign In" else "Create Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // ── Divider ───────────────────────────────────────────────
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                        Text("  or  ", color = TextGray, fontSize = 13.sp)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                    }

                    // ── Google Button ─────────────────────────────────────────
                    OutlinedButton(
                        onClick = { loginWith("Champion", "champion@gmail.com") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.DarkGray),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Continue with Google",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// ── Reusable styled text field ─────────────────────────────────────────────────
@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = PurpleAccent) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = TextGray
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType
        ),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor     = PurpleAccent,
            unfocusedBorderColor   = Color.DarkGray,
            focusedLabelColor      = PurpleAccent,
            unfocusedLabelColor    = TextGray,
            focusedTextColor       = Color.White,
            unfocusedTextColor     = Color.White,
            cursorColor            = PurpleAccent,
            focusedContainerColor  = SurfaceDark,
            unfocusedContainerColor = SurfaceDark
        )
    )
}