package com.radiantmood.calarm.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.constraintlayout.compose.ConstrainedLayoutReference

fun Modifier.ref(ref: ConstrainedLayoutReference) = layoutId(ref.id)



