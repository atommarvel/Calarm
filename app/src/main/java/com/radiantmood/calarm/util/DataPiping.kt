package com.radiantmood.calarm.util

fun<I,O> ((I) -> O).bind(input: I): () -> Unit {
    return {
        this(input)
    }
}