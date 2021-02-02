package com.radiantmood.calarm.util

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
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
fun LoadingScreen() = Fullscreen {
    CircularProgressIndicator()
}

@Composable
fun AppBarAction(imageVector: ImageVector, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(imageVector, null) // TODO: null
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