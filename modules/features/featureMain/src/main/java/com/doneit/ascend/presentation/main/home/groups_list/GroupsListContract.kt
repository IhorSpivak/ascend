package com.doneit.ascend.presentation.main.home.groups_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface GroupsListContract {
    interface ViewModel : BaseViewModel {
        val groups: LiveData<GroupListWithUserPaged>
        val user: LiveData<UserEntity?>
        val requestModel: LiveData<GroupListDTO>

        fun setGroupType(groupType: GroupType?)
        fun updateData(userId: Long? = null)
        fun updateRequestModel(requestModel: GroupListDTO)
        fun onStartChatClick(groupId: Long, groupType: GroupType)
        fun onGroupClick(groupId: Long)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
    }
}