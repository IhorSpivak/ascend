package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.models.PresentationCardModel
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

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
        val isSubscribed: LiveData<Boolean>
        val starting: LiveData<Boolean>
        val users: MutableLiveData<List<ParticipantEntity>>
        val supportTitle: LiveData<Int>
        val closeDialog: SingleLiveEvent<Boolean>


        fun onBackPressed()
        fun onShareInApp()
        fun loadData(groupId: Long)
        fun subscribe(card: PresentationCardModel)
        fun subscribe()
        fun joinToDiscussion()
        fun startGroup()
        fun deleteGroup()
        fun leaveGroup()
        fun cancelGroup(reason: String)
        fun inviteToGroup(reason: List<String>)
        fun report(content: String)
        fun onAddPaymentClick()
        fun onMMClick()
        fun onViewClick(attendees: List<AttendeeEntity>)
        fun onDuplicateClick(group: GroupEntity)
        fun onEditClick(group: GroupEntity)
        fun onCancelClick(group: GroupEntity)
        fun onUpdatePrivacyClick(isPrivate: Boolean)
        fun removeMember(attendee: AttendeeEntity)
    }

    interface Router {
        fun onBack()
        fun navigateToAddPaymentMethod()
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
        fun navigateToMMInfo(id: Long)
        fun navigateToViewAttendees(attendees: List<AttendeeEntity>, group: GroupEntity)
        fun navigateToEditGroup(group: GroupEntity)
        fun navigateToDuplicateGroup(group: GroupEntity)
        fun navigateToShare(
            id: Long,
            user: UserEntity,
            shareType: SharePostBottomSheetFragment.ShareType
        )
    }
}