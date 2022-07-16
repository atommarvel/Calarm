package com.radiantmood.calarm.screen.alarm

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
import com.radiantmood.calarm.common.DoNothingLambda
import com.radiantmood.calarm.common.withNonNull
import com.radiantmood.calarm.compose.ButtonUiState
import com.radiantmood.calarm.compose.Fullscreen
import com.radiantmood.calarm.compose.ModelContainerContent
import com.radiantmood.calarm.screen.ModelContainer

@Preview(showBackground = true)
@Composable
fun Preview_AlarmExperienceScreen() {
    AlarmExperienceScreen(
        uiStateContainer = AlarmExperienceScreenUiState(
            stopUiState = ButtonUiState(
                text = "Alarm",
                label = "Stop",
                action = DoNothingLambda
            ),
            scheduleAndStopUiState = ButtonUiState(
                text = "Schedule & Stop",
                label = "Set alarm for event start",
                action = DoNothingLambda
            )
        )
    )
}

@Composable
fun AlarmExperienceScreen(
    uiStateContainer: ModelContainer<AlarmExperienceScreenUiState>
) {
    ModelContainerContent(uiStateContainer) { uiState ->
        Fullscreen {
            with(uiState.stopUiState) {
                label?.let {
                    Text(label, fontSize = 32.sp, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = action) { Text(text, modifier = Modifier.padding(40.dp), fontSize = 24.sp) }
            }
            Spacer(modifier = Modifier.height(24.dp))
            withNonNull(uiState.scheduleAndStopUiState) {
                label?.let {
                    Text(label, textAlign = TextAlign.Center)
                }
                Button(onClick = action) { Text(text) }
            }
        }
    }
}