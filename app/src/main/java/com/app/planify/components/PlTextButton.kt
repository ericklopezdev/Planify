package com.app.planify.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.planify.ui.theme.PlColors
import com.app.planify.ui.theme.PlTypography

@Composable
fun PlTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            style = PlTypography.bodyMedium,
            color = PlColors.Primary
        )
    }
}
