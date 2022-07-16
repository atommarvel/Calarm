package com.radiantmood.calarm.screen.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.radiantmood.calarm.CalendarSelectionScreen
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.compose.UiStateContainerContent
import com.radiantmood.calarm.navigate
import com.radiantmood.calarm.screen.LoadingUiStateContainer
import com.radiantmood.calarm.screen.UiStateContainer
import com.radiantmood.calarm.screen.calendars.CalendarRow
import com.radiantmood.calarm.screen.calendars.CalendarSelectionUiState
import com.radiantmood.calarm.common.formatTime
import java.util.*

val LocalSettingsScreenViewModel = compositionLocalOf<SettingsViewModel> { error("No SettingsViewModel") }

@Composable
fun SettingsScreenRoot() {
    val vm: SettingsViewModel = viewModel()
    vm.getData()
    CompositionLocalProvider(
        LocalAppBarTitle provides "Settings",
        LocalSettingsScreenViewModel provides vm
    ) {
        SettingsScreen()
    }
}

@Composable
fun SettingsScreen() {
    val uiStateContainer: UiStateContainer<SettingsScreenUiState> by LocalSettingsScreenViewModel.current.settingsScreen.observeAsState(LoadingUiStateContainer())
    Column {
        CalarmTopAppBar()
        UiStateContainerContent(uiStateContainer) { SettingsList(it) }
    }
}

@Composable
fun SettingsList(screenModel: SettingsScreenUiState) {
    // show expanded by default if there are less than 5 selected calendars
    val (expanded, setExpanded) = remember { mutableStateOf(screenModel.selectedCalendars.size < 5) }
    LazyColumn {
        item {
            ViewCalendarsRow()
            Divider()
        }
        item {
            DailyNotifRow(screenModel.dailyNotifTime)
            Divider()
        }
        item {
            SelectedCalendarsHeader(screenModel.selectedCalendars.isNotEmpty(), expanded, setExpanded)
        }
        SelectedCalendars(expanded, screenModel.selectedCalendars)
        item {
            GitHubRow()
            // TODO: OSS atttribution
            Divider()
        }
    }
}

@Composable
fun DailyNotifRow(dailyNotif: Calendar?) {
    val onOrOff = if (dailyNotif != null) "ON" else "OFF"
    val vm = LocalSettingsScreenViewModel.current
    val context = LocalContext.current
    IconSettingsRow(icon = Icons.Default.Notifications, text = "Daily Notification: $onOrOff", isSwitchChecked = dailyNotif != null) {
        vm.toggleDailyNotifs()
    }
    if (dailyNotif != null) {
        Divider()
        IconSettingsRow(icon = Icons.Default.Schedule, text = "Daily notification will be sent at ${dailyNotif.formatTime()}") {
            showDailyNotifTimePicker(context, vm, dailyNotif.get(Calendar.HOUR_OF_DAY), dailyNotif.get(Calendar.MINUTE))
        }
    }
}

fun showDailyNotifTimePicker(context: Context, vm: SettingsViewModel, hour: Int, minute: Int) {
    val picker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(hour)
        .setMinute(minute)
        .setTitleText("Daily notification time")
        .build()
    (context as? AppCompatActivity)?.let {
        picker.addOnPositiveButtonClickListener {
            vm.setDailyNotificationHour(picker.hour, picker.minute)
        }
        picker.show(it.supportFragmentManager, "timepicker")
    }
}

@Composable
fun GitHubRow() {
    val context = LocalContext.current
    IconSettingsRow(icon = Icons.Default.Code, text = "Open-sourced on GitHub!") {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://github.com/atommarvel/Calarm")
        }
        context.startActivity(intent)
    }
}

@Composable
fun SelectedCalendarsHeader(shouldShow: Boolean, expanded: Boolean, setExpanded: (Boolean) -> Unit) {
    if (shouldShow) {
        val expandIconRotation = animateFloatAsState(targetValue = if (expanded) 0f else 180f)
        IconSettingsRow(icon = Icons.Default.ExpandLess, iconRotation = expandIconRotation.value, text = "Currently selected calendars:") {
            setExpanded(!expanded)
        }
        Divider()
    }
}

fun LazyListScope.SelectedCalendars(shouldShow: Boolean, calendars: List<CalendarSelectionUiState>) {
    if (shouldShow) {
        items(calendars) { calendar ->
            Box(modifier = Modifier.padding(start = 24.dp)) {
                CalendarRow(calendar)
            }
            Divider()
        }
    }
}

@Composable
fun ViewCalendarsRow() {
    val navController = LocalNavController.current
    IconSettingsRow(icon = Icons.Default.CalendarToday, contentDescription = "Calendar", text = "Add / remove calendars") {
        navController.navigate(CalendarSelectionScreen)
    }
}

@Composable
fun IconSettingsRow(
    icon: ImageVector,
    text: String,
    contentDescription: String? = null,
    iconRotation: Float? = null,
    textStyle: TextStyle = TextStyle.Default,
    isSwitchChecked: Boolean? = null,
    onClick: (() -> Unit)? = null
) {
    SettingsRow(onClick = onClick) {
        // TODO: extract rotating icon?
        val iconMod = iconRotation?.let {
            Modifier.graphicsLayer {
                rotationZ = it
            }
        } ?: Modifier
        Icon(imageVector = icon, modifier = iconMod, contentDescription = contentDescription)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = textStyle, modifier = Modifier.weight(1f))
        isSwitchChecked?.let {
            Switch(it, onCheckedChange = { onClick?.invoke() })
        }
    }
}

@Composable
fun SettingsRow(
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val modifier = onClick?.let { Modifier.clickable { it.invoke() } } ?: Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}