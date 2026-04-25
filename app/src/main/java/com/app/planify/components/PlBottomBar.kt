package com.app.planify.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.planify.constants.Routes
import com.app.planify.ui.theme.PlColors
import com.app.planify.ui.theme.PlTypography

private data class PlNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

private val navItems = listOf(
    PlNavItem("Home",     "home",     Icons.Outlined.Home,         Icons.Filled.Home),
    PlNavItem("Tasks",    "tasks",    Icons.Outlined.CheckCircle,  Icons.Filled.CheckCircle),
    PlNavItem("Pomodoro", "pomodoro", Icons.Outlined.Timer,        Icons.Filled.Timer),
    PlNavItem("Profile",  "profile",  Icons.Outlined.Person,       Icons.Filled.Person),
)

@Composable
fun PlBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = PlColors.Surface
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            // Always pop back to Home (without saving state)
                            // so any tab click clears the stack above Home first
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, style = PlTypography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PlColors.Primary,
                    selectedTextColor = PlColors.Primary,
                    indicatorColor = PlColors.Container,
                    unselectedIconColor = PlColors.TextHint,
                    unselectedTextColor = PlColors.TextHint
                )
            )
        }
    }
}
