package com.doneit.ascend.domain.entity.group

import android.os.Parcelable
import com.doneit.ascend.domain.entity.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class GroupEntity(
    override var id: Long,
    val name: String?,
    val description: String?,
    val startTime: Date?,
    val status: GroupStatus?,
    val groupType: GroupType?,
    val price: Float?,
    val image: ImageEntity?,
    val meetingsCount: Int?,
    val passedCount: Int,
    val createdAt: Date?,
    val updatedAt: Date?,
    val banner: Banner?,
    val owner: OwnerEntity?,
    val subscribed: Boolean?,
    val invited: Boolean?,
    val blocked: Boolean?,
    val participantsCount: Int?,
    val invitesCount: Int?,
    var daysOfWeek: List<CalendarDayEntity>,
    val note: NoteEntity?,
    val meetingFormat: String?,
    val tag: TagEntity?,
    val attendees: List<AttendeeEntity>?,
    val isPrivate: Boolean,
    val pastMeetingsCount: Int?,
    val dates: List<String>?,
    val themes: List<String>?,
    val duration: Int
) : SearchEntity(id), Parcelable {

    val timeInProgress: Long
        get() {
            val currentDate = Date()
            return currentDate.time - startTime!!.time
        }

    val timeToFinish: Long
        get() {
            val currentDate = Date()
            val time = startTime!!.time + duration.minutesToMillis() - currentDate.time
            return if (time > 0) time else 0
        }

    val inProgress: Boolean
        get() {
            return (getDefaultCalendar().time > startTime)
        }

    val isStarting: Boolean
        get() {
            return (startTime!!.time - getDefaultCalendar().timeInMillis) < UPCOMING_INTERVAL
        }

    private fun Calendar.plus(interval: Long): Calendar {
        val c = getDefaultCalendar()
        c.time = Date(this.time.time + interval)

        return c
    }

    fun copy(
        status: GroupStatus?
    ): GroupEntity {
        return GroupEntity(
            id,
            name,
            description,
            startTime,
            status ?: this.status,
            groupType,
            price,
            image,
            meetingsCount,
            passedCount,
            createdAt,
            updatedAt,
            banner,
            owner,
            subscribed,
            invited,
            blocked,
            participantsCount,
            invitesCount,
            daysOfWeek,
            note?.copy(),
            meetingFormat,
            tag,
            attendees,
            isPrivate,
            pastMeetingsCount,
            dates,
            themes,
            duration
        )
    }

    companion object {
        const val START_TIME_KEY = "start_time"

        const val PROGRESS_DURATION = 1 * 60 * 60 * 1000L //1hour
        const val FINISHING_INTERVAL = 5 * 60 * 1000L //5 minutes before finish
        private const val UPCOMING_INTERVAL = 10 * 60 * 1000L //10 min
    }
}


fun Int.minutesToMillis(): Long {
    return this * 60 * 1000L
}