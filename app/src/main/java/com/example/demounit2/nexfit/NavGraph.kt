package com.example.demounit2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.demounit2.components.BottomNavBar
import com.example.demounit2.nexfit.screens.SplashScreen
import com.example.demounit2.screens.*
import com.example.demounit2.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexFitTopBar(onProfileClick: () -> Unit, onNotificationClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "NEXFIT",
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                fontSize = 20.sp,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onProfileClick, modifier = Modifier.padding(start = 8.dp)) {
                Surface(
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    color = PurpleAccent.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, PurpleAccent.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onNotificationClick, modifier = Modifier.padding(end = 8.dp)) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = PurpleDeep, modifier = Modifier.size(6.dp))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = BackgroundBlack,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun NavigationDrawerContent(navController: NavController, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = PurpleAccent.copy(alpha = 0.15f),
                border = BorderStroke(2.dp, PurpleAccent)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                // Reads from UserSession — updated on login
                Text(
                    text = UserSession.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = UserSession.email,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            DrawerMenuItem(Icons.Default.Person, "Profile", onItemClick)
            DrawerMenuItem(Icons.Default.Dashboard, "Dashboard", onItemClick)
            DrawerMenuItem(Icons.Default.Settings, "Settings", onItemClick)
        }

        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Logout: clear session and return to auth
        DrawerMenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "Logout",
            onClick = {
                onItemClick()
                UserSession.displayName = "NexFit Champion"
                UserSession.email = "nexfit@fitness.com"
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            },
            textColor = Color(0xFFFF5252)
        )
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    textColor: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (textColor == Color.White) PurpleAccent else textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

// ── Route constants ────────────────────────────────────────────────────────────
private const val ROUTE_SPLASH          = "splash"
private const val ROUTE_AUTH            = "auth"
private const val ROUTE_HOME            = "home"   // ✅ plain route — matches BottomNavBar exactly
private const val ROUTE_WORKOUTS        = "workouts"
private const val ROUTE_PROGRESS        = "progress"
private const val ROUTE_DIET            = "diet"
private const val ROUTE_PROFILE         = "profile"
private const val ROUTE_NOTIFICATIONS   = "notifications"
private const val ROUTE_EXERCISE_SCREEN = "exercise_screen/{muscleGroup}"
private const val ROUTE_WORKOUT_LOGGER  = "workout_logger/{exerciseName}"

private val HIDDEN_CHROME_ROUTES = setOf(
    ROUTE_SPLASH,
    ROUTE_AUTH,
    ROUTE_WORKOUT_LOGGER,
    ROUTE_NOTIFICATIONS
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexFitRoot() {
    val navController = rememberNavController()
    val drawerState   = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope         = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showChrome   = currentRoute !in HIDDEN_CHROME_ROUTES

    ModalNavigationDrawer(
        drawerState     = drawerState,
        gesturesEnabled = showChrome,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BackgroundBlack,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                NavigationDrawerContent(
                    navController = navController,
                    onItemClick   = { scope.launch { drawerState.close() } }
                )
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Scaffold(
            containerColor = BackgroundBlack,
            topBar = {
                if (showChrome) {
                    NexFitTopBar(
                        onProfileClick      = { scope.launch { drawerState.open() } },
                        onNotificationClick = { navController.navigate(ROUTE_NOTIFICATIONS) }
                    )
                }
            },
            bottomBar = {
                if (showChrome) {
                    BottomNavBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = ROUTE_SPLASH,
                modifier         = Modifier.padding(innerPadding)
            ) {

                // ── Splash ────────────────────────────────────────────────────
                composable(ROUTE_SPLASH) {
                    SplashScreen(navController = navController)
                    // ⚠️ Make sure SplashScreen.kt navigates to "auth", not "home"
                }

                // ── Auth (Sign In / Sign Up) ───────────────────────────────────
                composable(ROUTE_AUTH) {
                    AuthScreen(navController = navController)
                }

                // ── Home ──────────────────────────────────────────────────────
                // UserSession.displayName is already set by AuthScreen before navigating here
                composable(ROUTE_HOME) {
                    HomeScreen(navController)
                }

                composable(ROUTE_WORKOUTS) { MuscleGroupScreen(navController) }
                composable(ROUTE_PROGRESS) { ProgressScreen(navController) }
                composable(ROUTE_DIET)     { DietTabScreen() }
                composable(ROUTE_PROFILE)  { ProfileScreen() }

                composable(
                    route = ROUTE_EXERCISE_SCREEN,
                    arguments = listOf(navArgument("muscleGroup") { type = NavType.StringType })
                ) { backStackEntry ->
                    val muscleGroup = backStackEntry.arguments?.getString("muscleGroup") ?: ""
                    ExerciseSelectionScreen(muscleGroup, navController)
                }

                composable(
                    route = ROUTE_WORKOUT_LOGGER,
                    arguments = listOf(navArgument("exerciseName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val exerciseName = backStackEntry.arguments?.getString("exerciseName") ?: ""
                    WorkoutLoggerScreen(exerciseName, navController)
                }

                composable(ROUTE_NOTIFICATIONS) {
                    NotificationScreen(navController)
                }
            }
        }
    }
}