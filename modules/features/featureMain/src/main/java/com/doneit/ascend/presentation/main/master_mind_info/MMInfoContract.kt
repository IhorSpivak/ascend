package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface MMInfoContract {

    interface ViewModel : BaseViewModel {
        val profile: LiveData<UserEntity?>
        val user: LiveData<UserEntity?>

        val showRatingBar: LiveData<Boolean>
        val enableFollow: LiveData<Boolean>
        val enableUnfollow: LiveData<Boolean>
        val isFollowVisible: LiveData<Boolean>
        val isUnfollowVisible: LiveData<Boolean>
        val rated: LiveData<Boolean>
        val myRating: LiveData<Int?>
        val sendReportStatus: SingleLiveManager<Boolean>
        val masterMindDescription: LiveData<String>

        fun getListOfTitles(): List<Int>
        fun onFollowClick()
        fun onUnfollowClick()
        fun setRating(rating: Int)
        fun onSeeGroupsClick()
        fun sendReport(content: String)

        fun setProfileId(id: Long)
        fun report(content: String)
        fun onShareInAppClick()
        fun startChatWithMM(mmId: Long)
        fun goBack()
    }

    interface Router {
        fun navigateToChat(chatEntity: ChatEntity, user: UserEntity, chatType: ChatType)
        fun onBack()
        fun navigateToGroupList(
            userId: Long?,
            groupType: GroupType?,
            isMyGroups: Boolean?,
            mmName: String?
        )

        fun navigateToShare(
            id: Long,
            user: UserEntity,
            shareType: SharePostBottomSheetFragment.ShareType
        )
    }
}