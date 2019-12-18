package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.dto.GroupType
import java.util.*


class GroupEntity(
    id: Long,
    val name: String?,
    val description: String?,
    val startTime: Date?,
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
    val participantsCount: Int?,
    val invitesCount: Int?,
    val daysOfWeek: List<CalendarDayEntity>?
) : SearchEntity(id) {

    val isStarting: Boolean
        get() {//todo refactor
            var res = false
            if(startTime != null && daysOfWeek != null) {
                if (passedCount < meetingsCount ?: 0) {
                    val startDate = Calendar.getInstance()
                    startDate.time = startTime
                    val currentDate = Calendar.getInstance()

                    val dayIndex = currentDate.get(Calendar.DAY_OF_WEEK) - 1
                    val day = CalendarDayEntity.values()[dayIndex]
                    if (daysOfWeek.contains(day)) {
                        startDate.set(Calendar.YEAR, 0)
                        startDate.set(Calendar.MONTH, 0)
                        startDate.set(Calendar.DAY_OF_MONTH, 0)

                        currentDate.set(Calendar.YEAR, 0)
                        currentDate.set(Calendar.MONTH, 0)
                        currentDate.set(Calendar.DAY_OF_MONTH, 0)

                        if (currentDate.after(startDate.plus(-1* UPCOMING_INTERVAL))
                            and currentDate.before(startDate.plus(UPCOMING_INTERVAL))) {
                            res = true
                        }
                    }
                }
            }

            return res
        }

    val inProgress: Boolean
        get() {//todo refactor
            var res  = false

            if(startTime != null && daysOfWeek != null) {

                if(passedCount < meetingsCount?:0) {
                    val startDate = Calendar.getInstance()
                    startDate.time = startTime
                    val currentDate = Calendar.getInstance()

                    val dayIndex = currentDate.get(Calendar.DAY_OF_WEEK) - 1
                    val day = CalendarDayEntity.values()[dayIndex]
                    if(daysOfWeek.contains(day)) {
                        startDate.set(Calendar.YEAR, 0)
                        startDate.set(Calendar.MONTH, 0)
                        startDate.set(Calendar.DAY_OF_MONTH, 0)

                        currentDate.set(Calendar.YEAR, 0)
                        currentDate.set(Calendar.MONTH, 0)
                        currentDate.set(Calendar.DAY_OF_MONTH, 0)

                        if(currentDate.after(startDate) and currentDate.before(startDate.plus(PROGRESS_DURATION))) {
                            res = true
                        }
                    }
                }
            }

            return res
        }

    private fun Calendar.plus(interval: Long): Calendar {
        val c = Calendar.getInstance()
        c.time = Date(this.time.time + interval)

        return c
    }

    companion object {
        private const val PROGRESS_DURATION = 1 * 60 * 60 * 1000L //1hour
        private const val UPCOMING_INTERVAL = 10 * 60 * 1000L //10 min
    }
}