package com.doneit.ascend.domain.entity.group

import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.SearchEntity
import java.util.*


class GroupEntity(
    id: Long,
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
    val owner: OwnerEntity?,
    val subscribed: Boolean?,
    val invited: Boolean?,
    val blocked: Boolean?,
    val participantsCount: Int?,
    val invitesCount: Int?,
    val daysOfWeek: List<CalendarDayEntity>?,
    val note: NoteEntity?
) : SearchEntity(id) {

    val timeInProgress: Long
        get() {
            val currentDate = Date()
            return currentDate.time - startTime!!.time
        }

    val timeToFinish: Long
        get() {
            val currentDate = Date()
            val time = startTime!!.time + PROGRESS_DURATION - currentDate.time
            return if (time > 0) time else 0
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
            owner,
            subscribed,
            invited,
            blocked,
            participantsCount,
            invitesCount,
            daysOfWeek,
            note?.copy()
        )
    }

    companion object {
        const val START_TIME_KEY = "start_time"

        const val PROGRESS_DURATION = 1 * 60 * 60 * 1000L //1hour
        const val FINISHING_INTERVAL = PROGRESS_DURATION - 5 * 60 * 1000L //5 minutes before finish
        private const val UPCOMING_INTERVAL = 10 * 60 * 1000L //10 min
    }
}