package com.radiantmood.calarm.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.ui.theme.CalarmTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun Fullscreen(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun CalarmTopAppBar(actions: @Composable () -> Unit = {}) {
    val title = LocalAppBarTitle.current
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { NavBack() },
        actions = { actions() })
}

@Composable
fun LoadingScreen() = Fullscreen {
    CircularProgressIndicator()
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

fun ComponentActivity.render(content: @Composable () -> Unit) {
    setContent {
        CalarmTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                content()
            }
        }
    }
}

@Composable
fun <T> composableFetch(subject: Any? = null, block: suspend CoroutineScope.() -> T): T? {
    var response by remember { mutableStateOf<T?>(null) }
    LaunchedEffect(subject) {
        response = block()
    }
    return response
}