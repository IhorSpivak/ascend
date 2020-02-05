package com.doneit.ascend.domain.entity.group

import android.os.Parcel
import android.os.Parcelable
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
    val daysOfWeek: List<CalendarDayEntity>?
) : SearchEntity(id), Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString(),
        description = parcel.readString(),
        startTime = parcel.readDate(),
        status = parcel.readGroupStatus(),
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
        blocked = parcel.readBool(),
        participantsCount = parcel.readInteger(),
        invitesCount = parcel.readInteger(),
        daysOfWeek = (parcel.readArrayList(Int::class.java.classLoader) as List<Int>?)?.map { CalendarDayEntity.values()[it] }
    )

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

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(id)
        p0?.writeString(name)
        p0?.writeString(description)
        p0?.writeDate(startTime)
        p0?.writeGroupStatus(status)
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
        p0?.writeBool(blocked)
        p0?.writeInteger(participantsCount)
        p0?.writeInteger(invitesCount)
        p0?.writeList(daysOfWeek?.map { it.ordinal })
    }

    override fun describeContents() = 0

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
            daysOfWeek
        )
    }

    companion object CREATOR : Parcelable.Creator<GroupEntity> {

        const val START_TIME_KEY = "start_time"

        const val PROGRESS_DURATION = 1 * 60 * 60 * 1000L //1hour
        const val FINISHING_INTERVAL = PROGRESS_DURATION - 5 * 60 * 1000L //5 minutes before finish
        private const val UPCOMING_INTERVAL = 10 * 60 * 1000L //10 min

        override fun createFromParcel(parcel: Parcel): GroupEntity {
            return GroupEntity(parcel)
        }

        override fun newArray(size: Int): Array<GroupEntity?> {
            return arrayOfNulls(size)
        }
    }
}