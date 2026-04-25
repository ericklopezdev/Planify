---
description: Generate the full navigation boilerplate for Planify (Routes singleton + AppNavigation + MainActivity wiring)
---

Set up the complete navigation structure for Planify. This command is run ONCE when starting the project. Do not re-run it if these files already exist — add new routes manually instead.

Generate three things:

---

## 1. `app/src/main/java/com/app/planify/constants/Routes.kt`

The `object Routes` is the single source of truth for every route string in the app.
It is a Kotlin `object` (singleton) — there is exactly one, and every screen references it.

```kotlin
package com.app.planify.constants

object Routes {
    // Auth flow
    const val AUTH        = "auth"
    const val OTP         = "otp/{email}"
    const val ONBOARDING  = "onboarding"

    // Main app
    const val HOME        = "home"
    const val TASKS       = "tasks"
    const val TASK_DETAIL = "tasks/{taskId}"
    const val ADD_TASK    = "tasks/add"
    const val POMODORO    = "pomodoro"
    const val PROFILE     = "profile"

    // Builder helpers — use these when calling navController.navigate()
    fun otp(email: String)       = "otp/$email"
    fun taskDetail(taskId: Int)  = "tasks/$taskId"
}
```

Rule: **never** write a route string as a raw literal anywhere in the codebase. Always use `Routes.CONSTANT` or `Routes.helper()`.

---

## 2. `app/src/main/java/com/app/planify/AppNavigation.kt`

One composable that owns the `NavController` and maps every route to its screen.
The `navController` is created here with `rememberNavController()` and **never leaves this file** — screens only receive lambda callbacks.

```kotlin
package com.app.planify

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.planify.constants.Routes

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH
    ) {

        // ── Auth flow ─────────────────────────────────────────────────────────

        composable(Routes.AUTH) {
            // AuthScreen(
            //     onNavigateToOtp = { email -> navController.navigate(Routes.otp(email)) }
            // )
        }

        composable(
            route = Routes.OTP,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            // OtpScreen(
            //     email = email,
            //     onNavigateToOnboarding = {
            //         navController.navigate(Routes.ONBOARDING) {
            //             popUpTo(Routes.AUTH) { inclusive = true }
            //         }
            //     },
            //     onNavigateToHome = {
            //         navController.navigate(Routes.HOME) {
            //             popUpTo(Routes.AUTH) { inclusive = true }
            //         }
            //     }
            // )
        }

        composable(Routes.ONBOARDING) {
            // OnboardingScreen(
            //     onNavigateToHome = {
            //         navController.navigate(Routes.HOME) {
            //             popUpTo(Routes.ONBOARDING) { inclusive = true }
            //         }
            //     }
            // )
        }

        // ── Main app ──────────────────────────────────────────────────────────

        composable(Routes.HOME) {
            // HomeScreen(
            //     onNavigateToTasks    = { navController.navigate(Routes.TASKS) },
            //     onNavigateToPomodoro = { navController.navigate(Routes.POMODORO) }
            // )
        }

        composable(Routes.TASKS) {
            // TasksScreen(
            //     onNavigateToDetail = { id -> navController.navigate(Routes.taskDetail(id)) },
            //     onNavigateToAdd    = { navController.navigate(Routes.ADD_TASK) }
            // )
        }

        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
            // TaskDetailScreen(
            //     taskId = taskId,
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Routes.ADD_TASK) {
            // AddTaskScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Routes.POMODORO) {
            // PomodoroScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Routes.PROFILE) {
            // ProfileScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
    }
}
```

The screen calls are commented out because the screens don't exist yet. Uncomment each one as you build the screen. The nav structure is complete from day one.

---

## 3. Update `app/src/main/java/com/app/planify/MainActivity.kt`

Replace the `setContent` block so `AppNavigation()` is the only thing rendered:

```kotlin
setContent {
    PlanifyTheme {
        AppNavigation()
    }
}
```

Remove the default `Greeting` composable and its `@Preview` — they are no longer needed.

---

## Rules for adding new routes later

1. Add the constant to `Routes.kt` first.
2. If the route has parameters, add a builder helper function in `Routes`.
3. Add the `composable(...)` block to `AppNavigation.kt`.
4. Uncomment / wire the screen call once the screen file exists.
5. **Never** navigate using raw strings — always `Routes.CONSTANT` or `Routes.helper()`.

## Navigation patterns reference

```kotlin
// Go to a screen (no args)
navController.navigate(Routes.TASKS)

// Go to a screen with an arg
navController.navigate(Routes.taskDetail(42))

// Go back
navController.popBackStack()

// Go to a screen and clear the backstack (e.g. after login)
navController.navigate(Routes.HOME) {
    popUpTo(Routes.AUTH) { inclusive = true }
}
```
