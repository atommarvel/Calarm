package com.radiantmood.calarm.compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.screen.ErrorModelContainer
import com.radiantmood.calarm.screen.FinishedModelContainer
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.ui.theme.CalarmTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

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

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : ModelContainer<T>> ModelContainerContent(
    modelContainer: ModelContainer<T>,
    finishedContent: @Composable (T) -> Unit
) {

    Crossfade(targetState = modelContainer.key, modifier = Modifier.fillMaxSize()) {
        val rememberedModelContainer = remember(it) { modelContainer }
        // rememberedModelContainer doesn't know the difference between two finished models. Always update to use the newer finished model, but don't crossfade between finished models.
        val model =
            if (rememberedModelContainer is FinishedModelContainer && modelContainer is FinishedModelContainer) modelContainer else rememberedModelContainer
        when (model) {
            is LoadingModelContainer<*> -> LoadingScreen()
            is ErrorModelContainer<*> -> ErrorScreen(model)
            is FinishedModelContainer<T> -> finishedContent(model as T)
        }
    }
}

@Composable
fun LoadingScreen() = Fullscreen {
    // Delay showing spinner by 300 ms. We only show spinner when things are taking a while.
    val showSpinner = composableFetch { delay(300); true } ?: false
    if (showSpinner) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(errorContainer: ErrorModelContainer<*>) = Fullscreen {
    val message = errorContainer.errorMessage ?: "An error has occured."
    Text(message)
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