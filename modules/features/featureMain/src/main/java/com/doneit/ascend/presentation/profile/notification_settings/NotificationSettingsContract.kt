package com.doneit.ascend.presentation.profile.notification_settings

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.user.PresentationUserModel

interface NotificationSettingsContract {
    interface ViewModel: BaseViewModel {
        val user: LiveData<PresentationUserModel?>

        fun onChanged(type: NotificationSettingsItem, value: Boolean)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}