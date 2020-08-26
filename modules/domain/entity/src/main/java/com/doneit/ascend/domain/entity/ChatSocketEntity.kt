package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity

data class MessageSocketEntity(
    val id: Long,
    val message: String?,
    val status: String?,
    val edited: Boolean?,
    val type: String?,
    val userId: Long?,
    val createdAt: String?,
    val updatedAt: String?,
    val event: ChatSocketEvent?,
    val post: Post?,
    val attachment: MessageAttachment?,
    val sharedUser: UserEntity?,
    val sharedGroup: GroupEntity?
)
