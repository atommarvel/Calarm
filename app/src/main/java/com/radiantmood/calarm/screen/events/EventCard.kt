package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.ui.theme.CalarmTheme
import com.radiantmood.calarm.util.LoremIpsum
import java.util.*

@Composable
fun EventCard(model: CalarmModel) {
    val rowModifier = Modifier.padding(horizontal = 16.dp)
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column {
            model.alarm?.let {
                EventCardHeader(model.alarm)
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeRangeLabel(model.event.timeRange, Modifier.weight(1f))
                CalendarDot(model.calendar.color)
                Spacer(Modifier.width(4.dp))
                CalendarLabel(model.calendar.name)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventLabel(label = model.event.name, modifier = Modifier.weight(1f))
                Switch(checked = model.alarm != null, onCheckedChange = { model.event.onToggleAlarm() })
            }
            model.event.debugData?.let { Row { Text(it) } }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun TimeRangeLabel(label: String, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides 0.8f) {
        Text(
            text = label,
            modifier = modifier,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun CalendarLabel(label: String) {
    CompositionLocalProvider(LocalContentAlpha provides 0.8f) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle2 // TODO: light?
        )
    }
}

@Composable
fun EventLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label,
        style = MaterialTheme.typography.h4,
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CalendarDot(color: Color) {
    Box(
        modifier = Modifier
            .background(color, CircleShape)
            .size(12.dp)
    )
}

private fun getPreviewCalarmModel(hasAlarm: Boolean = true): CalarmModel =
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

@Preview
@Composable
fun PreviewEventRowUnselected() {
    CalarmTheme {
        EventCard(getPreviewCalarmModel(false))
    }
}

@Preview
@Composable
fun PreviewEventRowSelected() {
    CalarmTheme {
        EventCard(getPreviewCalarmModel())
    }
}