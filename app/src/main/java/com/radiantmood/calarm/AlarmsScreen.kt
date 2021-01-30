package com.radiantmood.calarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.CalendarRepository.CalEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val TAG = "araiff"

@Composable
fun fetchCalEvents(): List<CalEvent>? = composableFetch {
    val calRepo = CalendarRepository()
    withContext(Dispatchers.Default) { calRepo.queryEvents() }
}

@Composable
fun AlarmsScreen(navController: NavController) {
    Permissions.current.checkPermission(navController)

    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            AppBarAction(drawableRes = R.drawable.ic_baseline_calendar_today_24) {
                navController.navigate("calendars")
            }
        })
        fetchCalEvents()?.let { events ->
            Log.d(TAG, "AlarmsScreen: events")
            LazyColumn {
                items(events) { event ->
                    Text(event.title)
                }
            }
        }
        // TODO: Loading
    }
}