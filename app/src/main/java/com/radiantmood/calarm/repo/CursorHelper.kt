package com.radiantmood.calarm.repo

import android.database.Cursor
import com.radiantmood.calarm.repo.CursorValueType.*

abstract class CursorHelper<T> : Iterable<T> {

    abstract val cursor: Cursor

    abstract val projections: List<Projection>

    abstract fun assemble(cursor: Cursor): T

    private val map by lazy {
        val map = mutableMapOf<Projection, Int>()
        projections.forEachIndexed { index, key ->
            map[key] = index
        }
        map
    }

    val keys by lazy { projections.map { it.first }.toTypedArray() }

    operator fun <V> get(key: Projection): V = map[key]?.let { index ->
        when (key.second) {
            STRING -> cursor.getString(index)
            INT -> cursor.getInt(index)
            LONG -> cursor.getLong(index)
        }
    } as V

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var index = 0

        override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

        override fun next(): T {
            cursor.moveToPosition(index)
            index++
            return assemble(cursor)
        }
    }
}

enum class CursorValueType {
    STRING, INT, LONG
}

typealias Projection = Pair<String, CursorValueType>

infix fun String.via(cursorValueType: CursorValueType) = Projection(this, cursorValueType)