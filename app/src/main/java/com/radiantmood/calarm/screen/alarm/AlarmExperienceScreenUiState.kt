package com.radiantmood.calarm.screen.alarm

import com.radiantmood.calarm.compose.ButtonUiState
import com.radiantmood.calarm.screen.FinishedUiStateContainer

data class AlarmExperienceScreenUiState(
    val stopUiState: ButtonUiState,
    val scheduleAndStopUiState: ButtonUiState? = null
) : FinishedUiStateContainer<AlarmExperienceScreenUiState>()