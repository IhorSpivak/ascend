package com.doneit.ascend.presentation.main.group_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.models.GroupListWithUser
import com.doneit.ascend.presentation.main.models.GroupListWithUserPaged

interface GroupListContract {
    interface ViewModel : ArgumentedViewModel<GroupListArgs> {
        val groups: LiveData<GroupListWithUserPaged>
        val groupType: LiveData<String>

        fun backClick()
    }

    interface Router {
        fun onBack()
    }
}