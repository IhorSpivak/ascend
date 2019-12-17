package com.doneit.ascend.presentation.main.home.group

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.home.group.common.GroupsArgs
import com.doneit.ascend.presentation.main.models.GroupListWithUser

interface GroupsContract {
    interface ViewModel : ArgumentedViewModel<GroupsArgs> {
        val groups: LiveData<GroupListWithUser>
        val groupName: LiveData<String>

        fun updateGroups()
        fun navigateToGroupList()
        fun onGroupClick(id: Long)
    }
}