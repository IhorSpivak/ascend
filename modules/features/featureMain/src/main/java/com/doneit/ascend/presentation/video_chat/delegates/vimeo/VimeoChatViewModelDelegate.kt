package com.doneit.ascend.presentation.video_chat.delegates.vimeo

import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatViewModelDelegate

class VimeoChatViewModelDelegate(viewModel: VideoChatViewModel) :
    VideoChatViewModelDelegate(viewModel) {
    override fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsDTO?,
        currentUser: UserEntity?
    ) {
        //TODO("Not yet implemented")
    }

    override fun clearChatResources() {
        //TODO("Not yet implemented")
    }

    override fun getCameraCount(): Int {
        //TODO("Not yet implemented")
        return 0
    }

}