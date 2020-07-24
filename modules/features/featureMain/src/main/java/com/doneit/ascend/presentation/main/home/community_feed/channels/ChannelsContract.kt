package com.doneit.ascend.presentation.main.home.community_feed.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.chat.ChannelsWithUser

interface ChannelsContract {
    interface ViewModel : BaseViewModel {
        val channelWithCurrentUser: MediatorLiveData<ChannelsWithUser>
        val channels: LiveData<PagedList<ChatEntity>>
        val filterTextAll: MutableLiveData<String>
        val user: LiveData<UserEntity?>
        fun onBackPressed()
        fun onNewChannelPressed()
        fun onNewChatPressed()
        fun onChannelPressed(channel: ChatEntity)
        fun onJoinChannel(channel: ChatEntity)
        fun onLeaveChannel(channel: ChatEntity)
    }

    interface Router {
        fun onBack()
        fun navigateToChannel(id: Long)
        fun navigateToNewChannel()
        fun navigateToNewChat()
    }
}