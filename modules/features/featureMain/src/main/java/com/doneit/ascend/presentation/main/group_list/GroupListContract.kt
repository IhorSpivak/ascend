package com.doneit.ascend.presentation.main.group_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs

interface GroupListContract {
    interface ViewModel : ArgumentedViewModel<GroupListArgs> {
        val groups: LiveData<List<GroupEntity>>
        val groupType: LiveData<String>

        fun backClick()
    }

    interface Router {
        fun onBack()
    }
}