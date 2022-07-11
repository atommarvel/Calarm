package com.radiantmood.calarm.util

inline fun <T, R> withNonNull(receiver: T?, block: T.() -> R): R? = receiver?.let { with(it, block) }