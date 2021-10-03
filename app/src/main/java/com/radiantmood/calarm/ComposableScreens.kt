package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import com.radiantmood.calarm.screen.PermissionsScreenRoot
import com.radiantmood.calarm.screen.calendars.CalendarsSelectionScreenRoot
import com.radiantmood.calarm.screen.events.EventsScreenRoot
import com.radiantmood.calarm.screen.settings.SettingsScreenRoot

sealed class ComposableScreen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val content: @Composable (NavBackStackEntry) -> Unit
)

object EventsScreen : ComposableScreen(route = "eventsScreen", content = { EventsScreenRoot() })

object CalendarSelectionScreen : ComposableScreen(route = "calendarSelectionScreen", content = { CalendarsSelectionScreenRoot() })

object SettingsScreen : ComposableScreen(route = "settingsScreen", content = { SettingsScreenRoot() })

object PermissionsScreen : ComposableScreen(route = "permissionsScreen", content = { PermissionsScreenRoot() })