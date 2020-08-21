package com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.webinars.common.WebinarFilter
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface MMLiveStreamsContract {
    interface ViewModel: BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val userLiveData: LiveData<UserEntity>
        val groups: LiveData<GroupListWithUserPaged>


        fun onStartChatClick(groupId: Long, groupType: GroupType)
        fun onGroupClick(model: GroupEntity)
        fun updateFilter(filter: WebinarFilter)
        fun checkUser(user: UserEntity)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
    }
}