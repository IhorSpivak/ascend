package com.doneit.ascend.domain.entity

import android.os.Parcel
import android.os.Parcelable
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
) : SearchEntity(id), Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString(),
        description = parcel.readString(),
        startTime = parcel.readDate(),
        groupType = parcel.readGroupType(),
        price = parcel.readF(),
        image = parcel.readParcelable<ImageEntity>(ImageEntity::class.java.classLoader),
        meetingsCount = parcel.readInteger(),
        passedCount = parcel.readInt(),
        createdAt = parcel.readDate(),
        updatedAt = parcel.readDate(),
        owner = parcel.readParcelable<OwnerEntity>(OwnerEntity::class.java.classLoader),
        subscribed = parcel.readBool(),
        invited = parcel.readBool(),
        participantsCount = parcel.readInteger(),
        invitesCount = parcel.readInteger(),
        daysOfWeek = (parcel.readArrayList(Int::class.java.classLoader) as List<Int>?)?.map { CalendarDayEntity.values()[it] }
    )

    val isStarting: Boolean
        get() {//todo refactor
            var res = false
            if (startTime != null && daysOfWeek != null) {
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

    val inProgress: Boolean
        get() {//todo refactor
            var res = false

            if (startTime != null && daysOfWeek != null) {

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

    val timeInProgress: Long
        get() {
            val currentDate = Date()
            return currentDate.time - startTime!!.time
        }

    val timeToFinish: Long
        get() {
            val currentDate = Date()
            val time = startTime!!.time + PROGRESS_DURATION - currentDate.time
            return if(time > 0) time else 0
        }

    private fun Calendar.plus(interval: Long): Calendar {
        val c = Calendar.getInstance()
        c.time = Date(this.time.time + interval)

        return c
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(id)
        p0?.writeString(name)
        p0?.writeString(description)
        p0?.writeDate(startTime)
        p0?.writeGroupType(groupType)
        p0?.writeF(price)
        p0?.writeParcelable(image, p1)
        p0?.writeInteger(meetingsCount)
        p0?.writeInt(passedCount)
        p0?.writeDate(createdAt)
        p0?.writeDate(updatedAt)
        p0?.writeParcelable(owner, p1)
        p0?.writeBool(subscribed)
        p0?.writeBool(invited)
        p0?.writeInteger(participantsCount)
        p0?.writeInteger(invitesCount)
        p0?.writeList(daysOfWeek?.map { it.ordinal })
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<GroupEntity> {
        const val PROGRESS_DURATION = 100 * 60 * 60 * 1000L //1hour
        const val FINISHING_INTERVAL = PROGRESS_DURATION - 5 * 60 * 1000L //5 minutes before finish
        private const val UPCOMING_INTERVAL = 1000 * 60 * 1000L //10 min todo change back correct values

        override fun createFromParcel(parcel: Parcel): GroupEntity {
            return GroupEntity(parcel)
        }

        override fun newArray(size: Int): Array<GroupEntity?> {
            return arrayOfNulls(size)
        }
    }
}