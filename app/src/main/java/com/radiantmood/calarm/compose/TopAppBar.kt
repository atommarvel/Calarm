package com.radiantmood.calarm.compose

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController

@Composable
fun CalarmTopAppBar(actions: @Composable () -> Unit = {}) {
    val title = LocalAppBarTitle.current
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { NavBack() },
        actions = { actions() })
}

@Composable
fun AppBarAction(imageVector: ImageVector, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(imageVector, null) // TODO: null
    }
}

@Composable
fun NavBack() {
    val navController = LocalNavController.current
    if (navController.previousBackStackEntry != null) {
        AppBarAction(imageVector = Icons.Default.ArrowBack, onClick = { navController.popBackStack() })
    }
}