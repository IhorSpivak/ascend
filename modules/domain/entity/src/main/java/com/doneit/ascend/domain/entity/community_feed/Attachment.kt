package com.doneit.ascend.domain.entity.community_feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attachment(
    val id: Long,
    val contentType: ContentType,
    val url: String,
    val thumbnail: String,
    val size: Size
) : Parcelable {

    fun isPortrait() = size.height > size.width
    fun isEqual() = size.height == size.width
}

@Parcelize
data class Size(
    val width: Int,
    val height: Int
) : Parcelable {
    fun aspectHeight(width: Int): Int {
        return ((width.toFloat() / this.width) * height).toInt()
    }
}