package com.radiantmood.calarm

fun <T> MutableCollection<T>.toggle(value: T) = if (contains(value)) remove(value) else add(value)