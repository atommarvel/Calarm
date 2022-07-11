package com.radiantmood.calarm.screen.alarm

import com.radiantmood.calarm.compose.ButtonUiState
import com.radiantmood.calarm.screen.FinishedModelContainer

data class AlarmExperienceScreenUiState(
    val stopUiState: ButtonUiState,
    val scheduleAndStopUiState: ButtonUiState? = null
) : FinishedModelContainer<AlarmExperienceScreenUiState>()