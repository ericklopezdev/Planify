package com.app.planify.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.planify.ui.theme.PlColors

@Composable
fun PlFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = PlColors.Primary,
        contentColor = PlColors.OnPrimary
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}
