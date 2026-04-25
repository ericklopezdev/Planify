package com.app.planify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PlLightColorScheme = lightColorScheme(
    primary            = PlPrimaryLight,
    onPrimary          = PlOnPrimaryLight,
    primaryContainer   = PlPrimaryContainerLight,
    onPrimaryContainer = PlOnPrimaryContainerLight,
    background         = PlBackgroundLight,
    surface            = PlSurfaceLight,
    onBackground       = PlOnBackgroundLight,
    onSurface          = PlOnSurfaceLight,
    outline            = PlOutlineLight,
    error              = PlErrorLight,
    onError            = PlOnErrorLight
)

private val PlDarkColorScheme = darkColorScheme(
    primary            = PlPrimaryDark,
    onPrimary          = PlOnPrimaryDark,
    primaryContainer   = PlPrimaryContainerDark,
    onPrimaryContainer = PlOnPrimaryContainerDark,
    background         = PlBackgroundDark,
    surface            = PlSurfaceDark,
    onBackground       = PlOnBackgroundDark,
    onSurface          = PlOnSurfaceDark,
    outline            = PlOutlineDark,
    error              = PlErrorDark,
    onError            = PlOnErrorDark
)

@Composable
fun PlanifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) PlDarkColorScheme else PlLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
