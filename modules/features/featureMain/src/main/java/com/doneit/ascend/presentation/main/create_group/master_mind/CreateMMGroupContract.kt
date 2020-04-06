package com.doneit.ascend.presentation.main.create_group.master_mind

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
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
        val membersToDelete: MutableLiveData<MutableList<AttendeeEntity>>
        val deletedMembers: MutableList<AttendeeEntity>

        fun onGroupSelected()
        fun onIndividualSelected()
        fun completeClick()
        fun updateGroup(group: GroupEntity)
        fun backClick()
        fun changeSchedule()
    }

    enum class Navigation {
        TO_GROUP,
        TO_INDIVIDUAL
    }
}