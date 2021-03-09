package com.radiantmood.calarm.screen.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiantmood.calarm.CalendarSelectionScreen
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.compose.ModelContainerContent
import com.radiantmood.calarm.navigate
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.screen.calendars.CalendarRow
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel

val LocalSettingsScreenViewModel = compositionLocalOf<SettingsViewModel> { error("No SettingsViewModel") }

// TODO: @previews on each screen file... or a single preview file?

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
    val modelContainer: ModelContainer<SettingsScreenModel> by LocalSettingsScreenViewModel.current.settingsScreen.observeAsState(LoadingModelContainer())
    Column {
        CalarmTopAppBar()
        ModelContainerContent(modelContainer) { SettingsList(it) }
    }
}

@Composable
fun SettingsList(screenModel: SettingsScreenModel) {
    // show expanded by default if there are less than 5 selected calendars
    val (expanded, setExpanded) = remember { mutableStateOf(screenModel.selectedCalendars.size < 5) }
    LazyColumn {
        item {
            ViewCalendarsRow()
            Divider()
        }
        item {
            SelectedCalendarsHeader(screenModel.selectedCalendars.isNotEmpty(), expanded, setExpanded)
        }
        SelectedCalendars(expanded, screenModel.selectedCalendars)
    }
}

@Composable
fun SelectedCalendarsHeader(shouldShow: Boolean, expanded: Boolean, setExpanded: (Boolean) -> Unit) {
    if (shouldShow) {
        val expandIconRotation = animateFloatAsState(targetValue = if (expanded) 0f else 180f)
        SettingsRow(icon = Icons.Default.ExpandLess, iconRotation = expandIconRotation.value, text = "Currently selected calendars:") {
            setExpanded(expanded)
        }
        Divider()
    }
}

fun LazyListScope.SelectedCalendars(shouldShow: Boolean, calendars: List<CalendarSelectionModel>) {
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
    SettingsRow(icon = Icons.Default.CalendarToday, contentDescription = "Calendar", text = "Add / remove calendars") {
        navController.navigate(CalendarSelectionScreen)
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    contentDescription: String? = null,
    iconRotation: Float? = null,
    text: String,
    textStyle: TextStyle = TextStyle.Default,
    onClick: (() -> Unit)? = null
) {
    val modifier = onClick?.let { Modifier.clickable { it.invoke() } } ?: Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconMod = iconRotation?.let {
            Modifier.graphicsLayer {
                rotationZ = it
            }
        } ?: Modifier
        Icon(imageVector = icon, modifier = iconMod, contentDescription = contentDescription)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = textStyle)
    }
}