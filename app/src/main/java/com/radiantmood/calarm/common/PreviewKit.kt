package com.radiantmood.calarm.common

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.repo.EventPart
import com.radiantmood.calarm.screen.events.AlarmUiState
import com.radiantmood.calarm.screen.events.CalarmUiState
import com.radiantmood.calarm.screen.events.CalendarUiState
import com.radiantmood.calarm.screen.events.EventUiState
import java.util.*

/**
 * Helpers for common preview functionality.
 */

object LoremIpsum {
    const val Short = "Aliquam et eleifend nibh."
    const val Medium = "Phasellus ut purus non massa pretium. Maecenas condimentum metus."
    const val Long =
        "Etiam placerat id ipsum sit amet rutrum. Nullam vel rhoncus urna, non malesuada ante. Integer ac odio non mauris scelerisque hendrerit ac ac purus. Duis risus purus, porttitor nec commodo at, fringilla et ex. Curabitur hendrerit accumsan nisi, blandit placerat purus sodales id."
}

fun getPreviewCalarmModel(hasAlarm: Boolean = true): CalarmUiState =
    CalarmUiState(
        event = EventUiState(
            name = LoremIpsum.Short,
            timeRange = "11:35am - 12:00pm",
            doesNextEventOverlap = false,
            onToggleAlarmStart = { },
            onToggleAlarmEnd = { },
        ),
        calendar = CalendarUiState(
            name = "Schedule",
            color = Color.Red
        ),
        alarms = if (hasAlarm) listOf(
            AlarmUiState(
                cal = Calendar.getInstance(),
                offset = -1L,
                eventPart = EventPart.START,
                onIncreaseOffset = {},
                onDecreaseOffset = {},
            )
        ) else emptyList()
    )