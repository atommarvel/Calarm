package com.radiantmood.calarm.util

fun <I1, O> ((I1) -> O).bind(input: I1): () -> Unit {
    return {
        this(input)
    }
}

fun <I1, I2, O> ((I1, I2) -> O).bind(input1: I1, input2: I2): () -> Unit {
    return {
        this(input1, input2)
    }
}