package com.doneit.ascend.presentation

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.models.PresentationCommunityModel
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

interface MainContract {
    interface ViewModel : BaseViewModel {
        val hasUnread: LiveData<Boolean>
        val hasUnreadMessages: LiveData<Boolean>
        val createGroupButtonVisibility: LiveData<Boolean>
        val communities: LiveData<List<PresentationCommunityModel>>
        val userShare: SingleLiveEvent<UserEntity>
        val user: LiveData<UserEntity?>

        fun saveCommunity(community: String, fragmentId: Int)
        fun getUnreadMessageCount()
        fun onNotificationClick()
        fun onSearchClick()
        fun onFilterClick()
        fun onChatClick()
        fun onShareClick()
        fun onShareInAppClick()

        fun startChat(groupId: Long)
        fun onCreateGroupClick()
        fun onHomeClick()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToCommunityFeed()
        fun navigateToProfile()
        fun tryToNavigateToGroupInfo(id: Long)
        fun tryToNavigateToProfile(id: Long)
    }

    interface Router {
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
        fun navigateToSearch()
        fun navigateToNotifications()
        fun navigateToGroupInfo(id: Long)
        fun navigateToLogin()
        fun navigateToMyChats()
        fun navigateToCommunityFeedFragment(user: UserEntity)
        fun navigateToCreateGroupMM()
        fun navigateToCreateGroup(type: GroupType)
        fun navigateToHome()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToMMProfile()
        fun navigateToRegularUserProfile()
        fun navigateToMMInfo(id: Long)
        fun navigateToShare(
            id: Long,
            user: UserEntity,
            shareType: SharePostBottomSheetFragment.ShareType
        )
    }
}