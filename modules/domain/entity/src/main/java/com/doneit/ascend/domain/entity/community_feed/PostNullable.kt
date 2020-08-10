package com.doneit.ascend.domain.entity.community_feed

import android.os.Parcelable
import com.doneit.ascend.domain.entity.OwnerEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PostNullable(
    val id: Long? = null,
    val attachments: List<Attachment>? = null,
    var commentsCount: Int? = null,
    val createdAt: Date? = null,
    val description: String? = null,
    var isLikedMe: Boolean? = null,
    val isOwner: Boolean? = null,
    var likesCount: Int? = null,
    val owner: OwnerEntity? = null,
    val updatedAt: Date? = null
): Parcelable {

    fun toPost() = Post(id ?: -1, attachments ?: emptyList(), 0, Date(), "", false, false, 0, OwnerEntity(0, "", null, 0f, false, null, false), Date())

    companion object {
        fun create(post: Post?) = if (post == null) PostNullable() else PostNullable(post.id, post.attachments, post.commentsCount, post.createdAt, post.description, post.isLikedMe, post.isOwner, post.likesCount, post.owner, post.updatedAt)
    }

}