package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.repo.CursorValueType.*
import java.lang.ClassCastException

/**
 * Streamlines:
 * - creating a [Cursor]
 * - providing an [Iterable] that extracts the [OUTPUT] results from a [Cursor] via [assemble]
 *
 * Use one per query to be made.
 */
abstract class CursorIterable<OUTPUT> : Iterable<OUTPUT> {

    /**
     * Each of the fields that [OUTPUT] needs to obtain from the cursor.
     */
    abstract val outputFields: List<OutputField>

    /**
     * Creates an instance of [OUTPUT], populating its fields with the help of [get].
     */
    abstract fun assemble(): OUTPUT

    /**
     * The value passed to the [ContentResolver.query] uri param.
     */
    abstract fun buildContentUri(): Uri

    /**
     * The value passed to the [ContentResolver.query] selection param.
     */
    abstract fun buildSelection(): String?

    /**
     * The value passed to the [ContentResolver.query] projection param.
     */
    private val projection: Array<String> by lazy { outputFields.map { it.first }.toTypedArray() }

    /**
     * The [Cursor] that powers the [Iterable].
     */
    private val cursor: Cursor by lazy { checkNotNull(query()) }

    /**
     * Each entry has:
     * - [Map.Entry.key]: The [OutputField] found in [outputFields]
     * - [Map.Entry.value]: The [Int] index of the [OutputField] within [outputFields]
     */
    private val outputFieldsIndexMap: Map<OutputField, Int> by lazy {
        val map = mutableMapOf<OutputField, Int>()
        outputFields.forEachIndexed { index, key ->
            map[key] = index
        }
        map
    }

    private fun query(): Cursor? {
        val contentResolver: ContentResolver = calarm.contentResolver
        // TODO: how to best deal with a null Cursor here?
        return contentResolver.query(buildContentUri(), projection, buildSelection(), null, null)
    }

    /**
     * Grabs the data from the [cursor] for the given [outputField].
     *
     * Will throw [ClassCastException] for [OutputField]s that don't map to the value type the [cursor] has for it.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(ClassCastException::class)
    operator fun <V> get(outputField: OutputField): V = outputFieldsIndexMap[outputField]?.let { index ->
        when (outputField.second) {
            STRING -> cursor.getString(index)
            INT -> cursor.getInt(index)
            LONG -> cursor.getLong(index)
        }
    } as V

    /**
     * Moves through [cursor] results.
     * Closes [cursor] after finishing iterating.
     */
    override fun iterator(): Iterator<OUTPUT> = object : Iterator<OUTPUT> {
        var index = 0

        override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

        override fun next(): OUTPUT {
            cursor.moveToPosition(index)
            index++
            return assemble()
        }
    }
}

/**
 * Values that [CursorIterable] knows how to extract from a [Cursor].
 */
enum class CursorValueType {
    STRING, INT, LONG
}

/**
 * A [Pair] where:
 * - [Pair.first] is utilized by [CursorIterable.projection]
 * - [Pair.second] is utilized by [CursorIterable.get] to ensure the correct [Cursor].getX is used
 */
typealias OutputField = Pair<String, CursorValueType>

/**
 * Syntactic sugar for making [CursorIterable] easier to read.
 */
infix fun String.via(cursorValueType: CursorValueType) = OutputField(this, cursorValueType)