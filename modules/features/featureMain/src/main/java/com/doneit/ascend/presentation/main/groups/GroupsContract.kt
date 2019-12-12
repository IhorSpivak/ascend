package com.doneit.ascend.presentation.main.groups

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import com.doneit.ascend.presentation.main.model.GroupListWithUser

interface GroupsContract {
    interface ViewModel : ArgumentedViewModel<GroupsArgs> {
        val groups: LiveData<GroupListWithUser>
    }

    interface Router
}