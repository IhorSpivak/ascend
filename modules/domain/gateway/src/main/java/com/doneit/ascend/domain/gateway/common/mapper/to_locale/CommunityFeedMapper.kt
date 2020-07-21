package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.community_feed.*
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.source.storage.local.data.community_feed.CommentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostAttachmentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments
import java.util.*


fun PostWithAttachments.toEntity(): Post {
    return with(postLocal) {
        Post(
            id = id,
            commentsCount = commentsCount,
            createdAt = Date(createdAt),
            description = description,
            isLikedMe = isLikedMe,
            isOwner = isOwner,
            likesCount = likesCount,
            owner = owner.toEntity(),
            updatedAt = Date(updatedAt),
            attachments = this@toEntity.attachments.map { it.toEntity() }
        )
    }
}

fun PostAttachmentLocal.toEntity(): Attachment {
    return Attachment(
        id = id,
        contentType = ContentType.values()[contentType],
        url = url,
        size = Size(width, height),
        thumbnail = thumbnail
    )
}

fun Post.toLocal(): PostWithAttachments {
    return PostWithAttachments(
        postLocal = PostLocal(
            id = id,
            commentsCount = commentsCount,
            createdAt = createdAt.time,
            description = description,
            isLikedMe = isLikedMe,
            isOwner = isOwner,
            likesCount = likesCount,
            owner = owner.toLocal(),
            updatedAt = updatedAt.time
        ),
        attachments = attachments.map { it.toLocal(id) }
    )
}

fun Comment.toLocal(): CommentLocal {
    return CommentLocal(
        id = id,
        createdAt = createdAt.time,
        isPostOwner = isPostOwner,
        postCommentsCount = postCommentsCount,
        text = text,
        user = user.toLocal()
    )
}

fun CommentLocal.toEntity(): Comment {
    return Comment(
        id = id,
        createdAt = Date(createdAt),
        isPostOwner = isPostOwner,
        postCommentsCount = postCommentsCount,
        text = text,
        user = user.toEntity()
    )
}

fun Attachment.toLocal(postId: Long): PostAttachmentLocal {
    return PostAttachmentLocal(
        id = id,
        contentType = contentType.ordinal,
        post_id = postId,
        url = url,
        width = size.width,
        height = size.height,
        thumbnail = thumbnail
    )
}