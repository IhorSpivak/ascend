package com.doneit.ascend.domain.entity.group

import com.doneit.ascend.domain.entity.*
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

    val inProgress: Boolean
        get() {//todo refactor
            var res = false

            if (startTime != null && daysOfWeek != null) {

                if (passedCount < meetingsCount ?: 0) {
                    val startDate = getDefaultCalendar()
                    startDate.time = startTime
                    val currentDate = getDefaultCalendar()

                    val dayIndex = currentDate.get(Calendar.DAY_OF_WEEK) - 1
                    val day = CalendarDayEntity.values()[dayIndex]
                    if (daysOfWeek.contains(day)) {
                        startDate.set(Calendar.YEAR, 0)
                        startDate.set(Calendar.MONTH, 0)
                        startDate.set(Calendar.DAY_OF_MONTH, 0)

                        currentDate.set(Calendar.YEAR, 0)
                        currentDate.set(Calendar.MONTH, 0)
                        currentDate.set(Calendar.DAY_OF_MONTH, 0)

                        if (currentDate.after(startDate) and currentDate.before(
                                startDate.plus(
                                    PROGRESS_DURATION
                                )
                            )
                        ) {
                            res = true
                        }
                    }
                }
            }

            return res
        }

    val isStarting: Boolean
        get() {//todo refactor
            var res = false
            if (startTime != null && daysOfWeek != null) {
                if (passedCount < meetingsCount ?: 0) {
                    val startDate = getDefaultCalendar()
                    startDate.time = startTime
                    val currentDate = getDefaultCalendar()

                    val dayIndex = currentDate.get(Calendar.DAY_OF_WEEK) - 1
                    val day = CalendarDayEntity.values()[dayIndex]
                    if (daysOfWeek.contains(day)) {
                        startDate.set(Calendar.YEAR, 0)
                        startDate.set(Calendar.MONTH, 0)
                        startDate.set(Calendar.DAY_OF_MONTH, 0)

                        currentDate.set(Calendar.YEAR, 0)
                        currentDate.set(Calendar.MONTH, 0)
                        currentDate.set(Calendar.DAY_OF_MONTH, 0)

                        if (currentDate.after(startDate.plus(-1 * UPCOMING_INTERVAL))
                            and currentDate.before(startDate.plus(UPCOMING_INTERVAL))
                        ) {
                            res = true
                        }
                    }
                }
            }

            return res
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