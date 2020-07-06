package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.source.storage.remote.data.response.community_feed.AttachmentResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.vrgsoft.core.gateway.orMinusOne
import com.vrgsoft.core.gateway.orZero
import java.util.*

fun PostResponse.toEntity(): Post {
    return Post(
        id = id.orMinusOne(),
        attachments = attachments.orEmpty().map { it.toEntity() },
        commentsCount = commentsCount.orZero(),
        createdAt = createdAt.orEmpty().toDate() ?: Date(),
        description = description.orEmpty(),
        isLikedMe = isLikedMe ?: false,
        isOwner = isOwner ?: false,
        likesCount = likesCount.orZero(),
        owner = owner?.toEntity() ?: OwnerEntity(
            id = -1,
            fullName = "",
            image = ImageEntity(
                url = "",
                thumbnail = ThumbnailEntity(
                    url = ""
                )
            ),
            rating = -1f,
            followed = false,
            location = null,
            connected = false
        ),
        updatedAt = updatedAt.orEmpty().toDate() ?: Date()
    )
}

fun AttachmentResponse.toEntity(): Attachment {
    return Attachment(
        id = id.orEmpty(),
        contentType = contentType.orEmpty(),
        url = url.orEmpty()
    )
}