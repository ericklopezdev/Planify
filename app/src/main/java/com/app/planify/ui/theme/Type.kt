package com.app.planify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// FontFamily.Serif usa "Noto Serif" en Android — estilo Times New Roman sin dependencias extra.
// Para cambiar a Lora (Google Fonts): agregar ui-text-google-fonts y reemplazar esta línea.
private val AppFont = FontFamily.Serif

val Typography = Typography(
    // Títulos de pantalla
    headlineLarge = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Bold,
        fontSize   = 22.sp,
        lineHeight = 30.sp
    ),
    // Títulos de sección / cards
    titleLarge = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 18.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 24.sp
    ),
    // Texto del cuerpo
    bodyLarge = TextStyle(
        fontFamily   = AppFont,
        fontWeight   = FontWeight.Normal,
        fontSize     = 15.sp,
        lineHeight   = 23.sp,
        letterSpacing = 0.1.sp
    ),
    bodyMedium = TextStyle(
        fontFamily   = AppFont,
        fontWeight   = FontWeight.Normal,
        fontSize     = 13.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // Etiquetas, botones, hints
    labelLarge = TextStyle(
        fontFamily   = AppFont,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily   = AppFont,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelSmall = TextStyle(
        fontFamily   = AppFont,
        fontWeight   = FontWeight.Normal,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.4.sp
    )
)
