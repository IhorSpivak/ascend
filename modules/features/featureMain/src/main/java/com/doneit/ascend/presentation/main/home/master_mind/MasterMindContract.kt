package com.doneit.ascend.presentation.main.home.master_mind

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.filter.FilterModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface MasterMindContract {
    interface ViewModel : BaseViewModel {
        val groups: LiveData<GroupListWithUserPaged>
        val requestModel: LiveData<GroupListDTO>
        val filter: FilterModel

        fun updateData()
        fun updateRequestModel(requestModel: GroupListDTO)
        fun onStartChatClick(groupId: Long, groupType: GroupType)
        fun onGroupClick(groupId: Long)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
    }
}