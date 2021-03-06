package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SearchContract {
    interface ViewModel: BaseViewModel {
        val groups: LiveData<PagedList<SearchEntity>>

        fun submitRequest(query: String)
        fun goBack()
        fun openGroupList(mm: MasterMindEntity)
        fun onMMClick(model: MasterMindEntity)
        fun onGroupClick(model: GroupEntity)
        fun onStartChatClick(groupId: Long, groupType: GroupType)
    }

    interface Router {
        fun onBack()
        fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?, mmName: String?)
        fun navigateToGroupInfo(id: Long)
        fun navigateToMMInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
    }
}