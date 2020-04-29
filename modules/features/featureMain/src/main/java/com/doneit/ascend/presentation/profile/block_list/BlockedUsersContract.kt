package com.doneit.ascend.presentation.profile.block_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface BlockedUsersContract {
    interface ViewModel : BaseViewModel {
        val filterTextAll: MutableLiveData<String>
        val blockedUsers: LiveData<PagedList<BlockedUserEntity>>
        fun onBackPressed()
        fun onUnblockUser(user: BlockedUserEntity)
    }

    interface Router {
        fun onBack()
    }
}