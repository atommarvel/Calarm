package com.radiantmood.calarm.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.radiantmood.calarm.compose.TextResource.IdTextResource
import com.radiantmood.calarm.compose.TextResource.StringTextResource

sealed class TextResource {
    class StringTextResource(val string: String) : TextResource()
    class IdTextResource(val id: Int) : TextResource()
}

fun String.toTextResource() = StringTextResource(this)
fun Int.toTextResource() = IdTextResource(this)

@Composable
@ReadOnlyComposable
fun textResource(textResource: TextResource): String =
    when (textResource) {
        is IdTextResource -> stringResource(textResource.id)
        is StringTextResource -> textResource.string
    }