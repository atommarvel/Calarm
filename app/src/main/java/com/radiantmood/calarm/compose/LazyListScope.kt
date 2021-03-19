package com.radiantmood.calarm.compose

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

fun LazyListScope.SectionTitle(shouldShow: Boolean, title: String, modifier: Modifier = Modifier) {
    if (shouldShow) {
        item {
            CompositionLocalProvider(LocalContentAlpha provides 0.60f) {
                Text(title, modifier = modifier, fontSize = 12.sp, style = MaterialTheme.typography.subtitle1)
            }
        }
    }
}