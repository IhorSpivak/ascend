package com.doneit.ascend.presentation.profile.common

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.user.PresentationUserModel
import com.doneit.ascend.presentation.profile.edit_bio.EditBioContract
import com.doneit.ascend.presentation.profile.master_mind.MMProfileContract
import com.doneit.ascend.presentation.profile.regular_user.UserProfileContract
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ProfileContract {
    interface ViewModel : BaseViewModel {

        val user: LiveData<PresentationUserModel?>
        val showPhotoDialog: SingleLiveManager<Unit>

        fun fetchData()
        fun onEditPhotoClick()
        fun onLogoutClick()
        fun onTermsClick()
        fun onPolicyClick()
        fun onSeeMyGroupsClick()
        fun onNotificationClick()
        fun hasIcon(): Boolean

        fun onAvatarSelected(sourceUri: Uri, destinationUri: Uri, fragmentToReceiveResult: Fragment)
        fun updateProfileIcon(path: String)
        fun removeProfileIcon()
        fun updateFullName(newFullName: String)
        fun onMMFollowedClick()
        fun onChangePhoneClick()
        fun onLocationClick()
        fun onChangePasswordClick()
        fun onEditEmailClick()
        fun onNotificationSettingsClick()
        fun onPaymentsClick(isMasterMind: Boolean)
    }

    interface Router : EditBioContract.Router, MMProfileContract.Router, UserProfileContract.Router {
        fun navigateToLogin()
        fun navigateToTerms()
        fun navigateToPrivacyPolicy()
        fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?, mmName: String?)
        fun navigateToNotifications()
        fun navigateToAvatarUCropActivity(
            sourceUri: Uri,
            destinationUri: Uri,
            fragmentToReceiveResult: Fragment
        )

        fun navigateToMMFollowed()
        fun navigateToChangePhone()
        fun navigateToChangeLocation(currentLocation: String?)
        fun navigateToChangePassword()
        fun navigateToEditEmail()
        fun navigateToNotificationSettings()
        fun navigateToPayments(isMasterMind: Boolean)
    }
}