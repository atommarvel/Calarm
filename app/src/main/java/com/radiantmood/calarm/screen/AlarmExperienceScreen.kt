package com.radiantmood.calarm.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.util.Fullscreen

@Composable
fun AlarmExperienceScreen(title: String, stopExperience: () -> Unit) {
    Fullscreen {
        Text(title)
        Spacer(modifier = Modifier.height(24.dp))
        Button(stopExperience) { Text("Stop") }
    }
}