package com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.WebinarCredentialsEntity
import com.doneit.ascend.domain.entity.user.UserEntity

interface IVimeoChatViewModelDelegate {
    fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: WebinarCredentialsEntity?,
        currentUser: UserEntity?
    )

    fun clearChatResources()
    fun getCameraCount(): Int
}