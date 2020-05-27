package com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo

import com.doneit.ascend.domain.entity.dto.WebinarCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel

class VimeoChatViewModelDelegate(val viewModel: WebinarVideoChatViewModel) :
    IVimeoChatViewModelDelegate {
    override fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: WebinarCredentialsDTO?,
        currentUser: UserEntity?
    ) {
        if (groupEntity != null) {
            viewModel.chatRole =
                if (currentUser!!.isMasterMind && groupEntity.owner!!.id == currentUser.id) {
                    viewModel.isVisitor.postValue(false)
                    ChatRole.OWNER
                } else {
                    viewModel.isVisitor.postValue(true)
                    ChatRole.VISITOR
                }
            viewModel.credentials.postValue(
                StartWebinarVideoModel(
                    viewModel.chatRole!!,
                    creds!!.chatId
                )
            )
            viewModel.changeState(VideoChatState.PREVIEW_DATA_LOADED)
        }
    }

    override fun clearChatResources() {
        //TODO("Not yet implemented")
    }

    override fun getCameraCount(): Int {
        //TODO("Not yet implemented")
        return 0
    }

}