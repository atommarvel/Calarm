package com.radiantmood.calarm.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun LazyListScope.SectionTitle(shouldShow: Boolean, title: String) {
    if (shouldShow) {
        item {
            Text(title, fontSize = 24.sp, modifier = Modifier.padding(12.dp), style = TextStyle(textDecoration = TextDecoration.Underline))
        }
    }
}