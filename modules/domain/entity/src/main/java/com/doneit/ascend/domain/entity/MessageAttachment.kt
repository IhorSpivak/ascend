package com.doneit.ascend.domain.entity

import android.os.Parcelable
import com.doneit.ascend.domain.entity.community_feed.ContentType
import kotlinx.android.parcel.Parcelize
import java.net.URLConnection

@Parcelize
data class MessageAttachment(
    val name: String,
    val size: String,
    val type: AttachmentType,
    val url: String
) : Parcelable {
    val contentType: ContentType?
        get() {
            return when {
                isImageFile() -> ContentType.IMAGE
                isVideoFile() -> ContentType.VIDEO
                else -> null
            }
        }

    fun isImageFile(): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(url)
        return mimeType.startsWith("image")
    }

    fun isVideoFile(): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(url)
        return mimeType.startsWith("video")
    }
}