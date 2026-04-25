package com.app.planify.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.planify.components.PlButton
import com.app.planify.components.PlInput
import com.app.planify.ui.theme.PlColors
import com.app.planify.ui.theme.PlSpacing
import com.app.planify.ui.theme.PlTypography

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToOtp: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlColors.Background)
            .padding(horizontal = PlSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PlLogo()

        Spacer(Modifier.height(PlSpacing.xl))

        // TODO: implementar Google Sign-In con GoogleSignInClient
        OutlinedButton(
            onClick = { /* TODO: Google Sign-In */ },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, PlColors.TextHint)
        ) {
            Text("G  ", color = PlColors.Primary, style = PlTypography.titleMedium)
            Text("Continuar con Google", style = PlTypography.labelLarge)
        }

        Spacer(Modifier.height(PlSpacing.md))
        PlDivider()
        Spacer(Modifier.height(PlSpacing.md))

        PlInput(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            label = "Correo electrónico"
        )

        Spacer(Modifier.height(PlSpacing.md))

        PlButton(
            text = "Continuar",
            enabled = viewModel.email.isNotBlank(),
            onClick = { viewModel.continuar(onNavigateToOtp) }
        )

        Spacer(Modifier.height(PlSpacing.sm))

        Text(
            text = "Ingresa tu email, detectamos si es cuenta nueva",
            style = PlTypography.bodyMedium,
            color = PlColors.TextHint,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PlLogo() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(PlColors.Primary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("P", style = PlTypography.headlineMedium, color = PlColors.OnPrimary)
    }
    Spacer(Modifier.height(PlSpacing.md))
    Text("Planify", style = PlTypography.headlineLarge, color = PlColors.TextMain)
    Text("tu kit de estudio", style = PlTypography.bodyMedium, color = PlColors.TextHint)
}

@Composable
private fun PlDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PlSpacing.sm)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = PlColors.TextHint.copy(alpha = 0.3f))
        Text("o", style = PlTypography.bodyMedium, color = PlColors.TextHint)
        HorizontalDivider(modifier = Modifier.weight(1f), color = PlColors.TextHint.copy(alpha = 0.3f))
    }
}
