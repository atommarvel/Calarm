package com.radiantmood.calarm.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiantmood.calarm.CalendarSelectionScreen
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.navigate
import com.radiantmood.calarm.screen.calendars.CalendarRow

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
    val screenModel: SettingsScreenModel by LocalSettingsScreenViewModel.current.settingsScreen.observeAsState(SettingsScreenModel.getEmpty())
    val navController = LocalNavController.current
    val hasSelectedCalendars = screenModel.selectedCalendars.isNotEmpty()
    Column {
        CalarmTopAppBar()
        LazyColumn {
            if (hasSelectedCalendars) {
                item {
                    Text(
                        "Currently Selected Calendars",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(12.dp),
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
            val label = if (hasSelectedCalendars) "View More Calendars" else "Select Calendars" // TODO: to vm
            item {
                // TODO: make a settings row
                Button(onClick = { navController.navigate(CalendarSelectionScreen) }) {
                    Text(label, modifier = Modifier.padding(12.dp))
                }
            }
            items(screenModel.selectedCalendars) { calendar ->
                CalendarRow(calendar)
                Divider()
            }
        }
    }
}