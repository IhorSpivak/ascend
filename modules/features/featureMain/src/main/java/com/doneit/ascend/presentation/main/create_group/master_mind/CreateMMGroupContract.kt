package com.doneit.ascend.presentation.main.create_group.master_mind

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.AddMemberArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface CreateMMGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs> {
        val createGroupModel: PresentationCreateGroupModel
        val navigation: LiveData<Navigation>
        val canComplete: LiveData<Boolean>

        fun onGroupSelected()
        fun onIndividualSelected()
        fun completeClick()
        fun backClick()
    }

    enum class Navigation {
        TO_GROUP,
        TO_INDIVIDUAL
    }
}