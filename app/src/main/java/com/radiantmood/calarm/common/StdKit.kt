package com.radiantmood.calarm.common

/**
 * Helpers for common std functions.
 */

inline fun <T, R> withNonNull(receiver: T?, block: T.() -> R): R? = receiver?.let { with(it, block) }