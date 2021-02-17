package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
import android.provider.CalendarContract.Calendars._ID
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.repo.CursorValueType.INT
import com.radiantmood.calarm.repo.CursorValueType.STRING


class CalendarRepository {

    @WorkerThread
    suspend fun queryCalendars(): List<UserCal> = NewCalendarCursor().map { it }

    class NewCalendarCursor : CursorHelper<UserCal>() {
        val id = _ID via INT
        val name = CALENDAR_DISPLAY_NAME via STRING

        override val projections: List<Projection> = listOf(
            id,
            name
        )

        override fun assemble(cursor: Cursor): UserCal =
            UserCal(
                id = this[id],
                name = this[name]
            )

        override val cursor: Cursor by lazy {
            val builder: Uri.Builder = Calendars.CONTENT_URI.buildUpon()
            val contentResolver: ContentResolver = calarm.contentResolver
            checkNotNull(contentResolver.query(builder.build(), keys, null, null, null))
        }
    }

    interface ICal {
        val id: Int
        val name: String
    }

    data class UserCal(override val id: Int, override val name: String) : ICal
}