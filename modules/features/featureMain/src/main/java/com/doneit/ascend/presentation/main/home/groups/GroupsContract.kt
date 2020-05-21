package com.doneit.ascend.presentation.main.home.groups

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface GroupsContract {
    interface ViewModel: BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val userLiveData: LiveData<UserEntity>
        val groups: LiveData<GroupListWithUserPaged>
        val tags: LiveData<List<TagEntity>>

        fun onStartChatClick(groupId: Long)
        fun onGroupClick(model: GroupEntity)
        fun updateFilter(filter: TagEntity? = null)
        fun checkUser(user: UserEntity)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long)
    }
}