package com.radiantmood.calarm.screen.calendars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.LocalPermissionsUtil
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.compose.UiStateContainerContent
import com.radiantmood.calarm.screen.LoadingUiStateContainer
import com.radiantmood.calarm.screen.UiStateContainer

val LocalCalendarsSelectionViewModel = compositionLocalOf<CalendarSelectionViewModel> { error("No CalendarSelectionViewModel") }

@Composable
fun CalendarsSelectionScreenRoot() {
    val navController = LocalNavController.current
    if (LocalPermissionsUtil.current.checkPermissions(navController)) return
    val vm: CalendarSelectionViewModel = viewModel()
    vm.getCalendarDisplays()
    CompositionLocalProvider(
        LocalAppBarTitle provides "Select Calendars to use",
        LocalCalendarsSelectionViewModel provides vm
    ) {
        CalendarsSelectionScreen()
    }
}

@Composable
fun CalendarsSelectionScreen() {
    val vm = LocalCalendarsSelectionViewModel.current
    val uiStateContainer: UiStateContainer<CalendarsSelectionScreenUiState> by vm.calendarsScreen.observeAsState(LoadingUiStateContainer())
    Column {
        CalarmTopAppBar()
        UiStateContainerContent(uiStateContainer) { screenModel ->
            CalendarList(screenModel.calendarSelectionUiStates)
        }
    }
}

@Composable
fun CalendarList(calendars: List<CalendarSelectionUiState>) {
    LazyColumn {
        items(calendars) { calendar ->
            CalendarRow(calendar)
            Divider()
        }
    }
}

@Composable
fun CalendarRow(calendar: CalendarSelectionUiState) {
    Row(
        modifier = Modifier
            .clickable(onClick = calendar.onCalendarToggled)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(calendar.color, CircleShape)
                .size(12.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = calendar.name, Modifier.weight(1f))
        Switch(checked = calendar.isSelected, onCheckedChange = { calendar.onCalendarToggled() })
    }
}