package com.doneit.ascend.domain.entity.community_feed

import android.os.Parcelable
import com.doneit.ascend.domain.entity.OwnerEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val id: Long,
    val attachments: List<Attachment>,
    val commentsCount: Int,
    val createdAt: Date,
    val description: String,
    var isLikedMe: Boolean,
    val isOwner: Boolean,
    var likesCount: Int,
    val owner: OwnerEntity,
    val updatedAt: Date
):Parcelable