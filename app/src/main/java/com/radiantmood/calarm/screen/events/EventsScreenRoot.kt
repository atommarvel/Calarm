package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.LocalPermissionsUtil
import com.radiantmood.calarm.compose.Fullscreen
import com.radiantmood.calarm.compose.UiStateContainerContent
import com.radiantmood.calarm.screen.LoadingUiStateContainer
import com.radiantmood.calarm.screen.UiStateContainer

val LocalEventsViewModel = compositionLocalOf<EventsViewModel> { error("No EventsViewModel") }

@Composable
fun EventsScreenRoot() {
    if (LocalPermissionsUtil.current.checkPermissions(LocalNavController.current)) return
    val eventsViewModel = viewModel<EventsViewModel>().apply {
        getData()
        autoRefresh()
    }
    CompositionLocalProvider(
        LocalAppBarTitle provides "Calarms", // TODO: Strings -> resource ids
        LocalEventsViewModel provides eventsViewModel
    ) {
        EventsScreen()
    }
}

@Composable
fun EventsScreen() {
    val uiStateContainer: UiStateContainer<EventsScreenUiState> by LocalEventsViewModel.current.eventsScreen.observeAsState(LoadingUiStateContainer())
    UiStateContainerContent(uiStateContainer) { screenModel ->
        when (screenModel) {
            is EventsScreenUiState.Eventful -> EventfulEventsScreen(screenModel)
            is EventsScreenUiState.FullscreenMessage -> FullscreenMessageEventsScreen(screenModel.message)
        }
    }
}

@Composable
fun FullscreenMessageEventsScreen(message: String) {
    Column {
        EventScreenTopBar()
        Fullscreen { Text(message) }
    }
}

