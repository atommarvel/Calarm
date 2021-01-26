package com.radiantmood.calarm

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun PermissionScreen(onClick: () -> Unit = {}) {
    Fullscreen {
        Text(text = "Calendar permission is required.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onClick() }) {
            Text(text = "Grant Permission")
        }
    }
}