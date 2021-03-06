package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.community_feed.*
import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.AttachmentResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommunityFeedEventMessage
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.SizeResponse
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
        id = id,
        contentType = ContentType.valueOf(contentType.orEmpty().toUpperCase(Locale.getDefault())),
        url = url.orEmpty(),
        size = size.toEntity(),
        thumbnail = thumbnail.orEmpty()
    )
}

fun CommentResponse.toEntity(): Comment {
    return Comment(
        id = id.orMinusOne(),
        createdAt = createdAt.orEmpty().toDate() ?: Date(),
        isPostOwner = isPostOwner ?: false,
        postCommentsCount = postCommentsCount.orZero(),
        text = text.orEmpty(),
        user = user?.toEntity() ?: BlockedUserEntity(id = -1, fullName = "", image = null)
    )
}

fun Attachment.toRequest(): AttachmentRequest {
    return AttachmentRequest(
        contentType = contentType.mimeType,
        url = url
    )
}

fun SizeResponse.toEntity(): Size {
    return Size(
        width = width.orZero(),
        height = height.orZero()
    )
}

fun CommunityFeedEventMessage.toEntity(): CommunityFeedSocketEntity {
    return CommunityFeedSocketEntity(
        postId = postId,
        commentsCount = commentsCount ?: -1,
        likesCount = likesCount ?: -1,
        event = CommunityFeedSocketEvent.fromRemoteString(event.orEmpty())
    )
}