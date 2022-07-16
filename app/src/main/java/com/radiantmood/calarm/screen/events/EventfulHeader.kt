package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.common.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun EventfulHeader(header: EventfulHeader) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        UpNext(header.nextAlarmStart)
        AlarmsLeft(header.alarmsLeft)
    }
}

@Composable
fun UpNext(nextAlarmStart: Calendar?) {
    nextAlarmStart?.let {
        val countDown = countDownProducer(nextAlarmStart)
        Column {
            Text(countDown.value, style = MaterialTheme.typography.h3)
            Text("until the next Calarm at ${nextAlarmStart.formatTime()}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
fun AlarmsLeft(alarmsLeft: String?) {
    alarmsLeft?.let {
        Column {
            Text(alarmsLeft, style = MaterialTheme.typography.h3)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun countDownProducer(cal: Calendar?): State<String> {
    return produceState(initialValue = "", key1 = cal) {
        launch {
            while (isActive) {
                cal?.let {
                    val diffMillis = it.timeInMillis - System.currentTimeMillis()
                    val hour = TimeUnit.MILLISECONDS.toHours(diffMillis)
                    val minute = TimeUnit.MILLISECONDS.toMinutes(diffMillis - TimeUnit.HOURS.toMillis(hour))
                    val second = TimeUnit.MILLISECONDS.toSeconds(diffMillis - TimeUnit.HOURS.toMillis(hour) - TimeUnit.MINUTES.toMillis(minute))
                    value = "$hour hours, $minute minutes, $second seconds"
                } ?: run {
                    value = ""
                }
                delay(1000)
            }
        }
    }
}