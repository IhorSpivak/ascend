package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCardModel

interface GroupInfoContract {
    interface ViewModel : BaseViewModel {
        val btnSubscribeVisible: LiveData<Boolean>
        val btnJoinVisible: LiveData<Boolean>
        val btnStartVisible: LiveData<Boolean>
        val btnDeleteVisible: LiveData<Boolean>
        val btnJoinedVisible: LiveData<Boolean>
        val group: LiveData<GroupEntity>
        val cards: LiveData<List<PresentationCardModel>>
        val isBlocked: Boolean
        val isSupport: LiveData<Boolean>
        val isEditable: LiveData<Boolean>
        val isMM: LiveData<Boolean>
        val isOwner: LiveData<Boolean>
        val starting: LiveData<Boolean>

        fun onBackPressed()
        fun loadData(groupId: Long)
        fun subscribe(card: PresentationCardModel)
        fun joinToDiscussion()
        fun startGroup()
        fun deleteGroup()
        fun cancelGroup(reason: String)
        fun inviteToGroup(reason: List<String>)
        fun report(content: String)
        fun onAddPaymentClick()
        fun onMMClick()
        fun onViewClick(attendees: List<AttendeeEntity>)
        fun onDuplicateClick(group: GroupEntity)
        fun onEditClick(group: GroupEntity)
        fun onCancelClick(group: GroupEntity)
        fun removeMember(attendee: AttendeeEntity)
    }

    interface Router {
        fun onBack()
        fun navigateToAddPaymentMethod()
        fun navigateToVideoChat(groupId: Long)
        fun navigateToMMInfo(id: Long)
        fun navigateToViewAttendees(attendees: List<AttendeeEntity>, groupId: Long)
        fun navigateToEditGroup(group: GroupEntity)
        fun navigateToDuplicateGroup(group: GroupEntity)
    }
}