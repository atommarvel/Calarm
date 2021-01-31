package com.radiantmood.calarm.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.AmbientMainViewModel
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.AmbientPermissions
import com.radiantmood.calarm.R
import com.radiantmood.calarm.repo.CalendarRepository.CalEvent
import com.radiantmood.calarm.util.AppBarAction
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlarmsScreen() {
    val navController = AmbientNavController.current
    AmbientPermissions.current.checkPermission(navController)

    val vm = AmbientMainViewModel.current
    val events: List<EventDisplay> by vm.eventDisplays.observeAsState(listOf())
    vm.getEventDisplays()

    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            AppBarAction(vectorResource(R.drawable.ic_baseline_calendar_today_24)) {
                navController.navigate("calendars")
            }
        })
        LazyColumn {
            items(events) { event ->
                EventRow(event)
            }
        }
        // TODO: Loading
    }
}

@Composable
fun EventRow(event: EventDisplay) {
    Row(
        modifier = Modifier
            .clickable(onClick = { /*TODO*/ })
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val dateFormat = SimpleDateFormat("hh:mma", Locale.getDefault())
        val startTime = dateFormat.format(event.calEvent.start.time)
        val endTime = dateFormat.format(event.calEvent.end.time)
        Text("\"${event.calEvent.title}\": $startTime - $endTime")
    }
}

data class EventDisplay(val calEvent: CalEvent)