package com.doneit.ascend.presentation.main.home.webinars

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface WebinarsContract {
    interface ViewModel : BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val userLiveData: LiveData<UserEntity>
        val groups: LiveData<GroupListWithUserPaged>

        fun onStartChatClick(groupId: Long, groupType: GroupType)
        fun onGroupClick(model: GroupEntity)
        fun updateFilter(filter: String, userId: Long? = null)
        fun checkUser(user: UserEntity)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
    }
}