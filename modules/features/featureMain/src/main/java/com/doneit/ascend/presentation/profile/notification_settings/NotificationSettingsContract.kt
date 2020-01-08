package com.doneit.ascend.presentation.profile.notification_settings

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotificationSettingsContract {
    interface ViewModel: BaseViewModel {
        val user: LiveData<ProfileEntity?>

        fun onChanged(type: NotificationSettingsItem, value: Boolean)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}