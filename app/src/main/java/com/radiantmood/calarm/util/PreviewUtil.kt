package com.radiantmood.calarm.util

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.screen.events.AlarmModel
import com.radiantmood.calarm.screen.events.CalarmModel
import com.radiantmood.calarm.screen.events.CalendarModel
import com.radiantmood.calarm.screen.events.EventModel
import java.util.*

object LoremIpsum {
    const val Short = "Aliquam et eleifend nibh."
    const val Medium = "Phasellus ut purus non massa pretium. Maecenas condimentum metus."
    const val Long =
        "Etiam placerat id ipsum sit amet rutrum. Nullam vel rhoncus urna, non malesuada ante. Integer ac odio non mauris scelerisque hendrerit ac ac purus. Duis risus purus, porttitor nec commodo at, fringilla et ex. Curabitur hendrerit accumsan nisi, blandit placerat purus sodales id."
}

fun getPreviewCalarmModel(hasAlarm: Boolean = true): CalarmModel =
    CalarmModel(
        event = EventModel(
            name = LoremIpsum.Short,
            timeRange = "11:35am - 12:00pm",
            doesNextEventOverlap = false,
            onToggleAlarm = { }
        ),
        calendar = CalendarModel(
            name = "Schedule",
            color = Color.Red
        ),
        alarm = if (hasAlarm) AlarmModel(
            cal = Calendar.getInstance(),
            offset = -1L,
            onIncreaseOffset = {},
            onDecreaseOffset = {}
        ) else null
    )