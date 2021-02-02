package com.radiantmood.calarm.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radiantmood.calarm.util.Fullscreen

@Preview(showBackground = true)
@Composable
fun PreviewAlarmExperienceScreen() {
    AlarmExperienceScreen(title = "Alarm", stopExperience = { /*TODO*/ })
}

@Composable
fun AlarmExperienceScreen(title: String, stopExperience: () -> Unit) {
    Fullscreen {
        Text(title, fontSize = 32.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = stopExperience) { Text("Stop", modifier = Modifier.padding(40.dp), fontSize = 24.sp) }
    }
}