package com.radiantmood.calarm.repo

import android.net.Uri
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Calendars.*
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.repo.CursorValueType.INT
import com.radiantmood.calarm.repo.CursorValueType.STRING


class CalendarRepository {

    data class UserCal(val id: Int, val name: String, val colorInt: Int)

    @WorkerThread
    fun queryCalendars(): List<UserCal> = CalendarCursorIterable().map { it }

    @WorkerThread
    fun getCalendar(targetId: Int): UserCal? = CalendarCursorIterable(targetId).firstOrNull()

    class CalendarCursorIterable(private val targetId: Int? = null) : CursorIterable<UserCal>() {

        val id = _ID via INT
        val name = CALENDAR_DISPLAY_NAME via STRING
        val color = CALENDAR_COLOR via INT

        override val outputFields: List<OutputField> = listOf(id, name, color)

        override fun assemble(): UserCal =
            UserCal(
                id = this[id],
                name = this[name],
                colorInt = this[color]
            )

        override fun buildContentUri(): Uri {
            val builder: Uri.Builder = Calendars.CONTENT_URI.buildUpon()
            targetId?.let { builder.appendPath(it.toString()) }
            return builder.build()
        }

        override fun buildSelection(): String? = null
    }
}