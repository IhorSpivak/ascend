package com.doneit.ascend.presentation.main.home.community_feed.share_post

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SharePostContract {
    interface ViewModel : BaseViewModel {

        /*fun getChats(filter: String): LiveData<PagedList<ChatEntity>>
        fun getChannels(filter: String): LiveData<PagedList<ChatEntity>>
        fun getUsers(filter: String): LiveData<PagedList<UserEntity>>*/
        fun onBackPressed()
    }
}