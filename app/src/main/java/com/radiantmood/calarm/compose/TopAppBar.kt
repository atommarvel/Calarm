package com.radiantmood.calarm.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController

@Composable
fun CalarmTopAppBar(actions: @Composable () -> Unit = {}) {
    val title = LocalAppBarTitle.current
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavBack()
        Text(
            text = title,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        actions()
    }

//    TopAppBar(
//        title = { Text(title) },
//        navigationIcon = { NavBack() },
//        actions = { actions() })
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