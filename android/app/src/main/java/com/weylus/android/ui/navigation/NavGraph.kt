package com.weylus.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * Navigation destinations
 */
sealed class Screen(val route: String) {
    data object Connection : Screen("connection")
    data object CapturableList : Screen("capturable_list")
    data object Video : Screen("video")
    data object Settings : Screen("settings")
}

/**
 * Main navigation graph for the app
 */
@Composable
fun WeylusNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Connection.route,
        modifier = modifier
    ) {
        composable(Screen.Connection.route) {
            // ConnectionScreen will be implemented later
            ConnectionScreenPlaceholder(
                onNavigateToCapturableList = {
                    navController.navigate(Screen.CapturableList.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.CapturableList.route) {
            // CapturableListScreen will be implemented later
            CapturableListScreenPlaceholder(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVideo = {
                    navController.navigate(Screen.Video.route) {
                        // Clear back stack when entering video screen
                        popUpTo(Screen.Connection.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(Screen.Video.route) {
            // VideoScreen will be implemented later
            VideoScreenPlaceholder(
                onNavigateBack = {
                    navController.popBackStack(Screen.Connection.route, inclusive = false)
                }
            )
        }

        composable(Screen.Settings.route) {
            // SettingsScreen will be implemented later
            SettingsScreenPlaceholder(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// Placeholder composables - will be replaced with actual screens
@Composable
private fun ConnectionScreenPlaceholder(
    onNavigateToCapturableList: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text("Connection Screen - Coming Soon")
        }
    }
}

@Composable
private fun CapturableListScreenPlaceholder(
    onNavigateBack: () -> Unit,
    onNavigateToVideo: () -> Unit
) {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text("Capturable List Screen - Coming Soon")
        }
    }
}

@Composable
private fun VideoScreenPlaceholder(onNavigateBack: () -> Unit) {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text("Video Screen - Coming Soon")
        }
    }
}

@Composable
private fun SettingsScreenPlaceholder(onNavigateBack: () -> Unit) {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text("Settings Screen - Coming Soon")
        }
    }
}

// Extension to make imports work
private fun Modifier.fillMaxSize() = androidx.compose.foundation.layout.fillMaxSize()
