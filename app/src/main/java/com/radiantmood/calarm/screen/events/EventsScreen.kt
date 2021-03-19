package com.radiantmood.calarm.screen.events


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.radiantmood.calarm.*
import com.radiantmood.calarm.compose.*
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.util.formatTime
import com.radiantmood.calarm.util.getDebugEvent
import com.radiantmood.calarm.util.getFutureCalendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

private val LocalEventsViewModel = compositionLocalOf<EventsViewModel> { error("No EventsViewModel") }
private val vm: EventsViewModel
    @Composable get() = LocalEventsViewModel.current
private val navController: NavHostController
    @Composable get() = LocalNavController.current

@Composable
fun EventsScreenRoot() {
    if (LocalPermissionsUtil.current.checkPermissions(navController)) return
    val eventsViewModel: EventsViewModel = viewModel()
    eventsViewModel.getData()
    CompositionLocalProvider(
        LocalAppBarTitle provides "Calarms", // TODO: Strings -> resource ids
        LocalEventsViewModel provides eventsViewModel
    ) {
        EventsScreen()
    }
}

@Composable
fun EventsScreen() {
    val modelContainer: ModelContainer<EventsScreenModel> by vm.eventsScreen.observeAsState(LoadingModelContainer())
    ModelContainerContent(modelContainer) { screenModel ->
        when (screenModel) {
            is EventsScreenModel.Eventful -> EventfulEventsScreen(screenModel)
            is EventsScreenModel.FullscreenMessage -> FullscreenMessageEventsScreen(screenModel.message)
        }
    }
}

@Composable
fun FullscreenMessageEventsScreen(message: String) {
    Column {
        TopBar()
        Fullscreen { Text(message) }
    }
}

@Composable
fun TopBar() {
    val vm = vm
    val navController = navController
    CalarmTopAppBar(actions = {
        // TODO: add a quick way to get to calendar app
        if (BuildConfig.DEBUG) {
            AppBarAction(imageVector = Icons.Default.BugReport) {
                // TODO: toggling debug and then flinging the list results in a compose embedded crash. Why?
                vm.toggleDebug()
            }
        }
        AppBarAction(Icons.Default.Settings) {
            navController.navigate(SettingsScreen)
        }
    })
}

@Composable
fun EventfulEventsScreen(screenModel: EventsScreenModel.Eventful) {
    if (screenModel.showDebugAlarmButton) DebugAlarmButton()
    LazyColumn {
        item { TopBar() }
        item { EventfulHeader(screenModel) }
        EventsList(screenModel.eventModels, screenModel.tmoEventModels, screenModel.unmappedAlarms)
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

// TODO: no alarm set
@Composable
fun EventfulHeader(screenModel: EventsScreenModel.Eventful) {
    val label = countDownProducer(screenModel.header.nextAlarmStart)
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(label.value, style = MaterialTheme.typography.h3)
        screenModel.header.nextAlarmStart?.formatTime()?.let { Text("until the next Calarm at $it", style = MaterialTheme.typography.body1) }
        Spacer(modifier = Modifier.height(14.dp))
        screenModel.header.alarmsLeft?.let { Text(it, style = MaterialTheme.typography.h3) }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

// TODO: move to settings screen
@Composable
fun DebugAlarmButton() {
    val vm = vm
    Button({
        val event = getDebugEvent(getFutureCalendar(secondsInFuture = 10))
        vm.scheduleAlarm(event.eventId, event.start, event.title)
    }) {
        Text("Schedule alarm 20 seconds from now")
    }
}

fun LazyListScope.EventsList(eventList: List<CalarmModel>, tmoEventList: List<CalarmModel>, alarmList: List<UnmappedAlarmModel>) {
    SectionTitle(eventList.isNotEmpty(), "Today", Modifier.padding(16.dp))
    items(eventList) { model ->
        EventRow(model)
        EventBottomSpacer(model.event.doesNextEventOverlap)
    }
    SectionTitle(tmoEventList.isNotEmpty(), "Tomorrow", Modifier.padding(16.dp))
    items(tmoEventList) { model ->
        EventRow(model)
        EventBottomSpacer(model.event.doesNextEventOverlap)
    }
    SectionTitle(alarmList.isNotEmpty(), "Unmapped Alarms", Modifier.padding(16.dp))
    items(alarmList) { unmappedAlarm ->
        UnmappedAlarmRow(unmappedAlarm)
    }
}

@Composable
fun EventBottomSpacer(doesNextEventOverlap: Boolean) {
    if (doesNextEventOverlap) {
        Box(
            modifier = Modifier
                .height(18.dp)
                .width(1.dp)
                .background(color = Color.Black.copy(alpha = 0.38f))
        )
    } else {
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun UnmappedAlarmRow(unmappedAlarm: UnmappedAlarmModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(unmappedAlarm.label, modifier = Modifier.weight(1f))
        Switch(checked = true, onCheckedChange = {
            unmappedAlarm.onRemoveAlarm()
        })
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
fun EventRow(model: CalarmModel) {
    val rowModifier = Modifier.padding(horizontal = 16.dp)
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier//.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
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
fun EventRowHeader(alarm: AlarmModel) {
    Surface(color = MaterialTheme.colors.secondary) {
        val constraintSet = ConstraintSet {
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

        ConstraintLayout(
            constraintSet,
            modifier = Modifier
                .background(color = Color.Transparent, MaterialTheme.shapes.small)
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(alarm.cal.formatTime(), modifier = Modifier.layoutId("alarmTime"), style = MaterialTheme.typography.subtitle2)
            Text(" - ", style = MaterialTheme.typography.subtitle2, modifier = Modifier
                .layoutId("minus")
                .padding(start = 32.dp))
            val beforeAfter = if (alarm.offset > 0) "after" else "before"
            Text(
                "Ring ${abs(alarm.offset)} minutes $beforeAfter",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.layoutId("offsetDescription")
            )
            Box(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .layoutId("midDescription"))
            Text(" + ", style = MaterialTheme.typography.subtitle2, modifier = Modifier
                .layoutId("plus")
                .padding(end = 16.dp))

            Box(
                Modifier
                    .layoutId("minusTarget")
                    .clickable { alarm.onDecreaseOffset() })
            Box(
                Modifier
                    .layoutId("plusTarget")
                    .clickable { alarm.onIncreaseOffset() })
        }
    }
}

@Composable
fun CalendarDot(color: Color) {
    Box(
        modifier = Modifier
            .background(color, CircleShape)
            .size(12.dp)
    )
}
