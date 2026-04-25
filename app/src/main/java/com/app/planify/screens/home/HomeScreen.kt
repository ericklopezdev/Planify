package com.app.planify.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.planify.components.PlCard
import com.app.planify.ui.theme.PlColors
import com.app.planify.ui.theme.PlSpacing
import com.app.planify.ui.theme.PlTypography

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToTasks: () -> Unit = {},
    onNavigateToPomodoro: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(PlSpacing.lg)
    ) {
        HomeHeader(userName = viewModel.userName)
        Spacer(Modifier.height(PlSpacing.lg))
        PendingTasksCard(count = viewModel.pendingTasksCount)
        Spacer(Modifier.height(PlSpacing.md))
        RecentTasksCard(tasks = viewModel.recentTasks)
        Spacer(Modifier.height(PlSpacing.lg))
        Text("Acceso rápido", style = PlTypography.titleMedium, color = PlColors.TextMain)
        Spacer(Modifier.height(PlSpacing.sm))
        QuickAccessRow(
            onNavigateToTasks = onNavigateToTasks,
            onNavigateToPomodoro = onNavigateToPomodoro
        )
    }
}

@Composable
private fun HomeHeader(userName: String) {
    Text(
        text = "¡Hola, $userName!",
        style = PlTypography.headlineMedium,
        color = PlColors.TextMain
    )
    Text(
        text = "¿Qué estudias hoy?",
        style = PlTypography.bodyMedium,
        color = PlColors.TextHint
    )
}

@Composable
private fun PendingTasksCard(count: Int) {
    PlCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Tareas pendientes", style = PlTypography.labelMedium, color = PlColors.TextHint)
                Spacer(Modifier.height(PlSpacing.xs))
                Text("$count tareas", style = PlTypography.headlineMedium, color = PlColors.TextMain)
            }
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = PlColors.Primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun RecentTasksCard(tasks: List<String>) {
    PlCard(modifier = Modifier.fillMaxWidth()) {
        Text("Recientes", style = PlTypography.labelMedium, color = PlColors.TextHint)
        Spacer(Modifier.height(PlSpacing.sm))
        tasks.forEach { title ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = PlSpacing.xs)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = PlColors.TextHint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "  $title",
                    style = PlTypography.bodyMedium,
                    color = PlColors.TextMain
                )
            }
        }
    }
}

@Composable
private fun QuickAccessRow(
    onNavigateToTasks: () -> Unit,
    onNavigateToPomodoro: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PlSpacing.md)
    ) {
        PlCard(
            modifier = Modifier.weight(1f),
            onClick = onNavigateToTasks
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = PlColors.Primary, modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(PlSpacing.xs))
                Text("Tareas", style = PlTypography.labelMedium, color = PlColors.TextMain)
            }
        }
        PlCard(
            modifier = Modifier.weight(1f),
            onClick = onNavigateToPomodoro
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Timer, contentDescription = null, tint = PlColors.Primary, modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(PlSpacing.xs))
                Text("Pomodoro", style = PlTypography.labelMedium, color = PlColors.TextMain)
            }
        }
    }
}
