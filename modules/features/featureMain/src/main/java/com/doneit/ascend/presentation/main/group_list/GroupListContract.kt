package com.doneit.ascend.presentation.main.group_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.model.GroupListWithUser

interface GroupListContract {
    interface ViewModel : ArgumentedViewModel<GroupListArgs> {
        val groups: LiveData<GroupListWithUser>
        val groupType: LiveData<String>

        fun backClick()
    }

    interface Router {
        fun onBack()
    }
}