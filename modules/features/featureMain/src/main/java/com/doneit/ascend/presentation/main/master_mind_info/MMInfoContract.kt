package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface MMInfoContract {

    interface ViewModel: BaseViewModel {
        val profile : LiveData<MasterMindEntity?>
        val user: LiveData<UserEntity?>

        val showRatingBar: LiveData<Boolean>
        val enableFollow: LiveData<Boolean>
        val enableUnfollow: LiveData<Boolean>
        val isFollowVisible: LiveData<Boolean>
        val isUnfollowVisible: LiveData<Boolean>
        val rated: LiveData<Boolean>
        val myRating: LiveData<Int?>
        val sendReportStatus: SingleLiveManager<Boolean>

        fun onFollowClick()
        fun onUnfollowClick()
        fun setRating(rating: Int)
        fun onSeeGroupsClick()
        fun sendReport(content: String)

        fun setMMId(id: Long)
        fun report(content: String)
        fun goBack()
    }

    interface Router {
        fun onBack()
        fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?, mmName: String?)
    }
}