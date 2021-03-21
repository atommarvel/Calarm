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
import com.radiantmood.calarm.util.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: no alarm set
@Composable
fun EventfulHeader(header: EventfulHeader) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        UpNext(header.nextAlarmStart)
        Spacer(modifier = Modifier.height(14.dp))
        header.alarmsLeft?.let { Text(it, style = MaterialTheme.typography.h3) }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun UpNext(nextAlarmStart: Calendar?) {
    val countDown = countDownProducer(nextAlarmStart)
    Column {
        Text(countDown.value, style = MaterialTheme.typography.h3)
        nextAlarmStart?.formatTime()?.let { Text("until the next Calarm at $it", style = MaterialTheme.typography.body1) }
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