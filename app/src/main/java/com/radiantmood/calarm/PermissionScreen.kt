package com.radiantmood.calarm

import android.Manifest.permission.READ_CALENDAR
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PermissionScreen(navController: NavController) {
    val permissions = Permissions.current
    Fullscreen {
        Text(text = "Calendar permission is required.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            GlobalScope.launch {
                permissions.requestPermission(READ_CALENDAR)
                withContext(Dispatchers.Main) {
                    navController.popBackStack()
                }
            }
        }) {
            Text(text = "Grant Permission")
        }
    }
}