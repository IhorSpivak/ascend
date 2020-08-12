package com.doneit.ascend.presentation.main.home.channels

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ChannelsContract {

    interface ViewModel: BaseViewModel {
        val communityList: LiveData<List<Community>>
        val channelList: LiveData<PagedList<ChatEntity>>

        fun fetchCommunityList()
    }

    interface Router {

    }

}