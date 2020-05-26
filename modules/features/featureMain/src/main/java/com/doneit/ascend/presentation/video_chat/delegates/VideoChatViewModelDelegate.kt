package com.doneit.ascend.presentation.video_chat.delegates

import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel

abstract class VideoChatViewModelDelegate(protected val viewModel: VideoChatViewModel) {

    abstract fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsDTO?,
        currentUser: UserEntity?
    )

    abstract fun clearChatResources()
    abstract fun getCameraCount(): Int
}