package com.radiantmood.calarm.screen.events

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import com.radiantmood.calarm.BuildConfig
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.SettingsScreen
import com.radiantmood.calarm.compose.AppBarAction
import com.radiantmood.calarm.compose.CalarmTopAppBar
import com.radiantmood.calarm.navigate

@Composable
fun EventScreenTopBar() {
    val vm = LocalEventsViewModel.current
    val navController = LocalNavController.current
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