package com.radiantmood.calarm.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.radiantmood.calarm.CalendarSelectionScreen
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.compose.ModelContainerContent
import com.radiantmood.calarm.compose.SectionTitle
import com.radiantmood.calarm.navigate
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.screen.calendars.CalendarRow
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel

val LocalSettingsScreenViewModel = compositionLocalOf<SettingsViewModel> { error("No SettingsViewModel") }

// TODO: @previews on each screen file

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
    val navController = LocalNavController.current
    val hasSelectedCalendars = screenModel.selectedCalendars.isNotEmpty()
    LazyColumn {
        SectionTitle(hasSelectedCalendars, "Currently Selected Calendars")
        if (hasSelectedCalendars) item { Spacer(modifier = Modifier.size(16.dp)) }
        ViewCalendarsRow(navController, hasSelectedCalendars)
        CalendarsList(screenModel.selectedCalendars)
    }
}


fun LazyListScope.ViewCalendarsRow(navController: NavController, hasSelectedCalendars: Boolean = false) {
    val label = if (hasSelectedCalendars) "Select More Calendars" else "Select Calendars" // TODO: to vm
    item {
        // TODO: make a settings row
        Button(onClick = { navController.navigate(CalendarSelectionScreen) }) {
            Text(label, modifier = Modifier.padding(12.dp))
        }
    }
}

fun LazyListScope.CalendarsList(calendars: List<CalendarSelectionModel>) {
    items(calendars) { calendar ->
        CalendarRow(calendar)
        Divider()
    }
}