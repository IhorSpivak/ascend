package com.doneit.ascend.presentation.main.create_group.master_mind

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface CreateMMGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs> {
        val createGroupModel: PresentationCreateGroupModel
        val navigation: LiveData<Navigation>
        val canComplete: LiveData<Boolean>
        val members: MutableLiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>

        fun onGroupSelected()
        fun onIndividualSelected()
        fun completeClick()
        fun updateGroup(id: Long)
        fun backClick()
        fun changeSchedule()
    }

    enum class Navigation {
        TO_GROUP,
        TO_INDIVIDUAL
    }
}