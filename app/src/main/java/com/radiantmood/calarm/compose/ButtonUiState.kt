package com.radiantmood.calarm.compose

data class ButtonUiState(
    val text: String,
    val label: String?,
    val action: () -> Unit
)
