package com.doneit.ascend.presentation.main.home.community_feed.share_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.models.community_feed.SharePostFilter
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

interface SharePostContract {
    interface ViewModel : BaseViewModel {

        val chats: LiveData<PagedList<ChatEntity>>
        val channels: LiveData<PagedList<ChatEntity>>
        val users: LiveData<PagedList<AttendeeEntity>>


        val filterTextAll: MutableLiveData<String>
        val sharePostFilter: MutableLiveData<SharePostFilter>
        val dismissDialog: SingleLiveEvent<Unit>

        fun shareChat(chatEntity: ChatEntity)
        fun shareToUser(userId: Long)

        fun updateSearch(filter: String)
        fun getChats(filter: String)
        fun getChannels(filter: String)
        fun getUsers(filter: String)
    }

    interface Router {
        fun navigateToSharedPostChat(chat: ChatEntity, user: UserEntity, chatType: ChatType)
        fun navigateToSharedPostChannel(channelId: Long)
    }
}