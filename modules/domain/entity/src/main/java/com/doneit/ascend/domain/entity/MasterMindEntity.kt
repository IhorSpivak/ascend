package com.doneit.ascend.domain.entity

import android.os.Parcel
import android.os.Parcelable

class MasterMindEntity(
    id: Long,
    val fullName: String?,
    val displayName: String?,
    val description: String?,
    val location: String?,
    val bio: String?,
    val groupsCount: Int?,
    val rating: Float,
    val followed: Boolean,
    val rated: Boolean,
    val image: ImageEntity?,
    val allowRating: Boolean?,
    val myRating: Int?
) : SearchEntity(id), Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        fullName = parcel.readString(),
        displayName = parcel.readString(),
        description = parcel.readString(),
        location = parcel.readString(),
        bio = parcel.readString(),
        groupsCount = parcel.readValue(Int::class.java.classLoader) as Int?,
        rating = parcel.readFloat(),
        followed = parcel.readInt() == 1,
        rated = parcel.readInt() == 1,
        image = parcel.readParcelable<ImageEntity>(ImageEntity::class.java.classLoader),
        allowRating = (parcel.readValue(Int::class.java.classLoader) as Int?) == 1,
        myRating = parcel.readValue(Int::class.java.classLoader) as Int?
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(id)
        p0?.writeString(fullName)
        p0?.writeString(displayName)
        p0?.writeString(description)
        p0?.writeString(location)
        p0?.writeString(bio)
        p0?.writeValue(groupsCount)
        p0?.writeFloat(rating)
        p0?.writeInt(followed.toInt())
        p0?.writeInt(rated.toInt())
        p0?.writeParcelable(image, p1)
        p0?.writeValue(allowRating?.toInt())
        p0?.writeValue(myRating)

    }

    override fun describeContents() = 0

    private fun Boolean.toInt() = if (this) 1 else 0

    companion object CREATOR : Parcelable.Creator<MasterMindEntity> {
        override fun createFromParcel(parcel: Parcel): MasterMindEntity {
            return MasterMindEntity(parcel)
        }

        override fun newArray(size: Int): Array<MasterMindEntity?> {
            return arrayOfNulls(size)
        }
    }
}