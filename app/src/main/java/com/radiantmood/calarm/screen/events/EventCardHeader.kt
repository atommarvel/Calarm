package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import com.radiantmood.calarm.util.formatTime
import com.radiantmood.calarm.util.ref
import kotlin.math.abs

@Composable
fun EventCardHeader(alarm: AlarmModel) {
    Surface(color = MaterialTheme.colors.secondary) {
        ConstraintLayout(
            eventRowHeaderConstraintSet,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = alarm.cal.formatTime(),
                modifier = Modifier.ref(ref.alarmTime),
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = " - ",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .ref(ref.minus)
                    .padding(start = 32.dp)
            )
            val beforeAfter = if (alarm.offset > 0) "after" else "before"
            Text(
                text = "Ring ${abs(alarm.offset)} minutes $beforeAfter", // TODO: right on time
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.ref(ref.offsetDescription)
            )
            Box(
                Modifier
                    .width(1.dp)
                    .ref(ref.midDescription)
            )
            Text(
                text = " + ",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .ref(ref.plus)
                    .padding(end = 16.dp)
            )

            Box(
                Modifier
                    .ref(ref.minusTarget)
                    .clickable { alarm.onDecreaseOffset() }
            )
            Box(
                Modifier
                    .ref(ref.plusTarget)
                    .clickable { alarm.onIncreaseOffset() }
            )
        }
    }
}

private val ref by lazy {
    object {
        val alarmTime by lazy { ConstrainedLayoutReference("alarmTime") }
        val minus by lazy { ConstrainedLayoutReference("minus") }
        val minusTarget by lazy { ConstrainedLayoutReference("minusTarget") }
        val offsetDescription by lazy { ConstrainedLayoutReference("offsetDescription") }
        val plus by lazy { ConstrainedLayoutReference("plus") }
        val plusTarget by lazy { ConstrainedLayoutReference("plusTarget") }
        val midDescription by lazy { ConstrainedLayoutReference("midDescription") }
    }
}

private val eventRowHeaderConstraintSet: ConstraintSet by lazy {
    ConstraintSet {
        fun ConstrainScope.topBotLinkToParent(topMargin: Dp = 12.dp, bottomMargin: Dp = 8.dp) {
            top.linkTo(parent.top, topMargin)
            bottom.linkTo(parent.bottom, bottomMargin)
        }

        constrain(ref.alarmTime) {
            topBotLinkToParent()
            start.linkTo(parent.start)
        }

        constrain(ref.plus) {
            topBotLinkToParent()
            end.linkTo(parent.end)
        }

        constrain(ref.offsetDescription) {
            topBotLinkToParent()
            end.linkTo(ref.plus.start, 4.dp)
        }

        constrain(ref.midDescription) {
            topBotLinkToParent()
            start.linkTo(ref.offsetDescription.start)
            end.linkTo(ref.offsetDescription.end)
        }

        constrain(ref.minus) {
            topBotLinkToParent()
            end.linkTo(ref.offsetDescription.start, 4.dp)
        }

        constrain(ref.plusTarget) {
            topBotLinkToParent(0.dp, 0.dp)
            end.linkTo(parent.end)
            start.linkTo(ref.midDescription.end)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }

        constrain(ref.minusTarget) {
            topBotLinkToParent(0.dp, 0.dp)
            start.linkTo(ref.minus.start)
            end.linkTo(ref.midDescription.start)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
    }
}