package com.doneit.ascend.presentation.video_chat.delegates.twilio

import com.doneit.ascend.domain.entity.group.GroupCredentialsEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity

interface ITwilioChatViewModelDelegate {

    fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsEntity?,
        currentUser: UserEntity?
    )

    fun clearChatResources()
    fun getCameraCount(): Int
}