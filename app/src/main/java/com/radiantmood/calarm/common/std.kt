package com.radiantmood.calarm.common

inline fun <T, R> withNonNull(receiver: T?, block: T.() -> R): R? = receiver?.let { with(it, block) }