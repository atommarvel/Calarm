package com.radiantmood.calarm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate

fun NavGraphBuilder.composableScreen(composableScreen: ComposableScreen) {
    composable(composableScreen.route, composableScreen.arguments, composableScreen.deepLinks, composableScreen.content)
}

fun NavController.navigate(composableScreen: ComposableScreen, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(composableScreen.route, builder)
}