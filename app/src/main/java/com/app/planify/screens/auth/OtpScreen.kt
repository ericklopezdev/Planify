package com.app.planify.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.planify.components.PlButton
import com.app.planify.components.PlTextButton
import com.app.planify.ui.theme.PlColors
import com.app.planify.ui.theme.PlSpacing
import com.app.planify.ui.theme.PlTypography

@Composable
fun OtpScreen(
    email: String,
    viewModel: OtpViewModel = viewModel(),
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlColors.Background)
            .padding(horizontal = PlSpacing.lg),
    ) {
        Spacer(Modifier.height(PlSpacing.lg))

        IconButton(onClick = onNavigateBack) {
            Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Volver", tint = PlColors.TextMain)
        }

        Spacer(Modifier.height(PlSpacing.lg))

        Text("Código de verificación", style = PlTypography.headlineMedium, color = PlColors.TextMain)
        Spacer(Modifier.height(PlSpacing.xs))
        Text(
            text = "Enviamos un código a\n$email",
            style = PlTypography.bodyMedium,
            color = PlColors.TextHint
        )

        Spacer(Modifier.height(PlSpacing.xl))

        OtpBoxes(
            code = viewModel.code,
            onCodeChange = viewModel::onCodeChange
        )

        Spacer(Modifier.height(PlSpacing.xl))

        PlButton(
            text = "Verificar código",
            enabled = viewModel.code.length == 6,
            onClick = {
                viewModel.verify(
                    email = email,
                    onNewUser = onNavigateToOnboarding,
                    onExistingUser = onNavigateToHome
                )
            }
        )

        Spacer(Modifier.height(PlSpacing.md))

        PlTextButton(
            text = "Reenviar código",
            onClick = { viewModel.resend(email) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "válido por 5 minutos",
            style = PlTypography.labelSmall,
            color = PlColors.TextHint,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun OtpBoxes(code: String, onCodeChange: (String) -> Unit) {
    BasicTextField(
        value = code,
        onValueChange = onCodeChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(PlSpacing.sm)
            ) {
                repeat(6) { i ->
                    val char = code.getOrNull(i)
                    val isActive = i == code.length
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = if (isActive) 2.dp else 1.dp,
                                color = when {
                                    isActive   -> PlColors.Primary
                                    char != null -> PlColors.Primary.copy(alpha = 0.6f)
                                    else        -> PlColors.TextHint.copy(alpha = 0.4f)
                                },
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char?.toString() ?: "",
                            style = PlTypography.titleLarge,
                            color = PlColors.TextMain,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}
