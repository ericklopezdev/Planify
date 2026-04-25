package com.app.planify.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// Wrapper sobre MaterialTheme.typography.
// Uso: PlTypography.headlineLarge, PlTypography.bodyMedium, etc.
object PlTypography {
    val headlineLarge  @Composable get() = MaterialTheme.typography.headlineLarge
    val headlineMedium @Composable get() = MaterialTheme.typography.headlineMedium
    val titleLarge     @Composable get() = MaterialTheme.typography.titleLarge
    val titleMedium    @Composable get() = MaterialTheme.typography.titleMedium
    val bodyLarge      @Composable get() = MaterialTheme.typography.bodyLarge
    val bodyMedium     @Composable get() = MaterialTheme.typography.bodyMedium
    val labelLarge     @Composable get() = MaterialTheme.typography.labelLarge
    val labelMedium    @Composable get() = MaterialTheme.typography.labelMedium
    val labelSmall     @Composable get() = MaterialTheme.typography.labelSmall
}
