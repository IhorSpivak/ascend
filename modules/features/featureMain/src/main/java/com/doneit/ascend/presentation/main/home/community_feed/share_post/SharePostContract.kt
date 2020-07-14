package com.doneit.ascend.presentation.main.home.community_feed.share_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.community_feed.SharePostFilter

interface SharePostContract {
    interface ViewModel : BaseViewModel {

        val chats: LiveData<PagedList<ChatEntity>>
        val channels: LiveData<PagedList<ChatEntity>>
        val users: LiveData<PagedList<UserEntity>>

        val filterTextAll: MutableLiveData<String>
        val sharePostFilter: MutableLiveData<SharePostFilter>

        fun shareChat(chatId: Long)
        fun onBackPressed()
    }

    interface Router{
        fun navigateToSharedPostChat(chatId: Long)
        fun navigateToSharedPostChannel(channelId: Long)
        fun navigateToSharedPostUser(userId: Long)
    }
}