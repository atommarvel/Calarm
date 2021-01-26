package com.radiantmood.calarm

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import com.eazypermissions.coroutinespermission.PermissionManager
import com.radiantmood.calarm.CalendarRepository.UserCal
import com.radiantmood.calarm.ui.theme.CalarmTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.Permission

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            renderScreen()
        }
    }

    private suspend fun renderScreen() {
        if (isCalendarPermissionGranted()) {
            renderPermissionRequired()
        } else {
            fetchAndRenderCalendars()
        }
    }

    private fun isCalendarPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PERMISSION_GRANTED

    private suspend fun fetchAndRenderCalendars() {
        val calRepo = CalendarRepository()
        val calendars = withContext(Dispatchers.Default) { calRepo.queryCalendars() }
        renderCalendars(calendars)
    }

    private fun renderCalendars(calendars: List<UserCal>) {
        setContent {
            CalarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ShowCalendars(calendars)
                }
            }
        }
    }

    private fun renderPermissionRequired() {
        setContent {
            CalarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PermissionView {
                        Log.d("araiff", "renderPermissionRequired: clicked")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionView(onClick: () -> Unit = {}) {
    Column {
        Text(text = "Calendar permission is required.")
        Button(onClick = {  }) {
            Text(text = "Grant Permission")
        }
    }
}


@Composable
fun ShowCalendars(calendarList: List<UserCal>) {
    LazyColumnFor(items = calendarList) { calendar ->
        CalendarRow(calendar = calendar)
        Divider()
    }
}

@Composable
fun CalendarRow(calendar: UserCal) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onCalendarClick(calendar) })
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = calendar.name)
    }
}

fun onCalendarClick(calendar: UserCal) {
    Log.d("araiff", "onCalendarClick: $calendar")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalarmTheme {
        ShowCalendars(
            calendarList = listOf(
                UserCal(0, "NVC Retreat"),
                UserCal(0, "NVC Retreat2"),
                UserCal(0, "NVC Retrea3")
            )
        )
    }
}