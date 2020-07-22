package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateChannelModel

interface CreateChannelContract {
    interface ViewModel : BaseViewModel {
        val newChannelModel: PresentationCreateChannelModel
        val canComplete: LiveData<Boolean>
        val connectionAvailable: MutableLiveData<Boolean>

        fun setEditMode(channel: ChatEntity)
        fun addMembers()
        fun complete()
    }

    interface Router {
        fun onBackWithOpenChannel(channel: ChatEntity)
        fun onBack()
    }
    interface LocalRouter {
        fun onBack()
        fun navigateToAddChannelMembers()
    }
}