package com.doneit.ascend.presentation.main.create_group.select_group_type

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SelectGroupTypeContract {
    interface ViewModel : BaseViewModel {
        val createGroupTitle: LiveData<Int>
        val supportTitle: LiveData<Int>
        fun selectGroupType(type: GroupType)
        fun backClick()
    }

    interface Router {
        fun onBack()
        fun navigateToCreateGroup(type: GroupType)
    }
}