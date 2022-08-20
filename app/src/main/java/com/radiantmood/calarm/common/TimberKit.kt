package com.radiantmood.calarm.common

import com.radiantmood.calarm.log.AppendFileTree
import timber.log.Timber

fun Timber.Forest.appendToFile(msg: String) {
    tag(AppendFileTree.TAG_SUFFIX)
    i(msg)
}