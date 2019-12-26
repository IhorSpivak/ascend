package com.doneit.ascend.presentation.profile

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ProfileContract {
    interface ViewModel : BaseViewModel {

        val user: LiveData<ProfileEntity?>
        val showPhotoDialog: SingleLiveManager<Unit>
        val showDeleteButton: LiveData<Boolean>

        fun onEditPhotoClick()
        fun onLogoutClick()
        fun onTermsClick()
        fun onPolicyClick()
        fun onSeeMyGroupsClick()
        fun onNotificationClick()

        fun deleteAccount()
        fun updateProfileIcon(path: String?)
        fun onAvatarSelected(sourceUri: Uri, destinationUri: Uri, fragmentToReceiveResult: Fragment)
    }

    interface Router {
        fun navigateToLogin()
        fun navigateToTerms()
        fun navigateToPrivacyPolicy()
        fun navigateToGroupList(groupType: GroupType?, isMyGroups: Boolean?, isAllGroups: Boolean)
        fun navigateToNotifications()
        fun navigateToAvatarUCropActivity(sourceUri: Uri, destinationUri: Uri, fragmentToReceiveResult: Fragment)
    }
}