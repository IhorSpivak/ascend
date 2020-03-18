package com.doneit.ascend.domain.entity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MasterMindEntity(
    override var id: Long,
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

   /* private constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        fullName = parcel.readString(),
        displayName = parcel.readString(),
        description = parcel.readString(),
        location = parcel.readString(),
        bio = parcel.readString(),
        groupsCount = parcel.readInteger(),
        rating = parcel.readFloat(),
        followed = parcel.readBool()!!,
        rated = parcel.readBool()!!,
        image = parcel.readParcelable<ImageEntity>(ImageEntity::class.java.classLoader),
        allowRating = parcel.readBool(),
        myRating = parcel.readInteger()
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(id)
        p0?.writeString(fullName)
        p0?.writeString(displayName)
        p0?.writeString(description)
        p0?.writeString(location)
        p0?.writeString(bio)
        p0?.writeInteger(groupsCount)
        p0?.writeFloat(rating)
        p0?.writeBool(followed)
        p0?.writeBool(rated)
        p0?.writeParcelable(image, p1)
        p0?.writeBool(allowRating)
        p0?.writeInteger(myRating)

    }*/

    companion object{
        const val FULL_NAME_KEY = "fullName"
    }
    /*override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<MasterMindEntity> {
        const val FULL_NAME_KEY = "fullName"

        override fun createFromParcel(parcel: Parcel): MasterMindEntity {
            return MasterMindEntity(parcel)
        }

        override fun newArray(size: Int): Array<MasterMindEntity?> {
            return arrayOfNulls(size)
        }
    }*/
}
