package com.radiantmood.calarm.common

import com.radiantmood.calarm.calarm

// TODO: use
sealed class TextResource {
    abstract fun asString(): CharSequence
}

fun String.toTextResource() = StringTextResource(this)
class StringTextResource(private val string: String) : TextResource() {
    override fun asString() = string
}

fun Int.toTextResource() = IdTextResource(this)
class IdTextResource(private val id: Int) : TextResource() {
    override fun asString(): String = calarm.getString(id)
}