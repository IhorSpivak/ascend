package com.doneit.ascend.domain.entity

import android.os.Parcel
import com.doneit.ascend.domain.entity.dto.GroupType
import java.util.*

fun Parcel.writeDate(value: Date?) {
    this.writeValue(value?.time)
}

fun Parcel.readDate(): Date? {
    val time = this.readValue(Long::class.java.classLoader) as Long?
    return if(time == null) null else Date(time)
}

fun Parcel.writeGroupType(value: GroupType?) {
    this.writeValue(value?.ordinal)
}

fun Parcel.readGroupType(): GroupType? {
    val ordinal = this.readValue(Int::class.java.classLoader) as Int?
    return if(ordinal == null) null else GroupType.values()[ordinal]
}

fun Parcel.writeInteger(value: Int?) {
    this.writeValue(value)
}

fun Parcel.readInteger(): Int? {
    return this.readValue(Int::class.java.classLoader) as Int?
}

fun Parcel.writeF(value: Float?) {
    this.writeValue(value)
}

fun Parcel.readF(): Float? {
    return this.readValue(Float::class.java.classLoader) as Float?
}

fun Parcel.writeBool(value: Boolean?) {
    this.writeValue(value)
}

fun Parcel.readBool(): Boolean? {
    return this.readValue(Boolean::class.java.classLoader) as Boolean?
}