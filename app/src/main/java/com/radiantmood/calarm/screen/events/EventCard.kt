package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.radiantmood.calarm.ui.theme.CalarmTheme
import com.radiantmood.calarm.util.LoremIpsum
import com.radiantmood.calarm.util.formatTime
import java.util.*
import kotlin.math.abs

@Composable
fun EventCard(model: CalarmModel) {
    val rowModifier = Modifier.padding(horizontal = 16.dp)
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column {
            model.alarm?.let {
                EventRowHeader(model.alarm)
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

@Composable
fun EventRowHeader(alarm: AlarmModel) {
    Surface(color = MaterialTheme.colors.secondary) {
        ConstraintLayout(
            eventRowHeaderConstraints,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = alarm.cal.formatTime(),
                modifier = Modifier.layoutId("alarmTime"),
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = " - ",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .layoutId("minus")
                    .padding(start = 32.dp)
            )
            val beforeAfter = if (alarm.offset > 0) "after" else "before"
            Text(
                text = "Ring ${abs(alarm.offset)} minutes $beforeAfter", // TODO: right on time
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.layoutId("offsetDescription")
            )
            Box(
                Modifier
                    .width(1.dp)
                    .layoutId("midDescription")
            )
            Text(
                text = " + ",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .layoutId("plus")
                    .padding(end = 16.dp)
            )

            Box(
                Modifier
                    .layoutId("minusTarget")
                    .clickable { alarm.onDecreaseOffset() }
            )
            Box(
                Modifier
                    .layoutId("plusTarget")
                    .clickable { alarm.onIncreaseOffset() }
            )
        }
    }
}

val eventRowHeaderConstraints: ConstraintSet by lazy {
    ConstraintSet {
        val alarmTime = createRefFor("alarmTime")
        val minus = createRefFor("minus")
        val minusTarget = createRefFor("minusTarget")
        val offsetDescription = createRefFor("offsetDescription")
        val plus = createRefFor("plus")
        val plusTarget = createRefFor("plusTarget")
        val midDescription = createRefFor("midDescription")

        fun ConstrainScope.topBotLinkToParent(topMargin: Dp = 12.dp, bottomMargin: Dp = 8.dp) {
            top.linkTo(parent.top, topMargin)
            bottom.linkTo(parent.bottom, bottomMargin)
        }

        constrain(alarmTime) {
            topBotLinkToParent()
            start.linkTo(parent.start)
        }

        constrain(plus) {
            topBotLinkToParent()
            end.linkTo(parent.end)
        }

        constrain(offsetDescription) {
            topBotLinkToParent()
            end.linkTo(plus.start, 4.dp)
        }

        constrain(midDescription) {
            topBotLinkToParent()
            start.linkTo(offsetDescription.start)
            end.linkTo(offsetDescription.end)
        }

        constrain(minus) {
            topBotLinkToParent()
            end.linkTo(offsetDescription.start, 4.dp)
        }

        constrain(plusTarget) {
            topBotLinkToParent(0.dp, 0.dp)
            end.linkTo(parent.end)
            start.linkTo(midDescription.end)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }

        constrain(minusTarget) {
            topBotLinkToParent(0.dp, 0.dp)
            start.linkTo(minus.start)
            end.linkTo(midDescription.start)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
    }
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