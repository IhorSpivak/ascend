package com.doneit.ascend.presentation.main.groups

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs

interface GroupsContract {
    interface ViewModel : ArgumentedViewModel<GroupsArgs> {
        val groups: LiveData<List<GroupEntity>>

        fun updateGroups()
    }

    interface Router
}