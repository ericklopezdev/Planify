package com.app.planify.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Wrapper sobre MaterialTheme.colorScheme.
// Cambia solo de oscuro a claro automáticamente según el sistema.
// Uso: PlColors.Primary, PlColors.Surface, etc.
object PlColors {
    val Primary    @Composable get() = MaterialTheme.colorScheme.primary
    val OnPrimary  @Composable get() = MaterialTheme.colorScheme.onPrimary
    val Container  @Composable get() = MaterialTheme.colorScheme.primaryContainer
    val Background @Composable get() = MaterialTheme.colorScheme.background
    val Surface    @Composable get() = MaterialTheme.colorScheme.surface
    val TextMain   @Composable get() = MaterialTheme.colorScheme.onBackground
    val TextHint   @Composable get() = MaterialTheme.colorScheme.outline
    val Error      @Composable get() = MaterialTheme.colorScheme.error
    val OnError    @Composable get() = MaterialTheme.colorScheme.onError
}
