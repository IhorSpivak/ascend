package com.doneit.ascend.presentation.profile.notification_settings

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotificationSettingsContract {
    interface ViewModel: BaseViewModel {
        val user: LiveData<UserEntity?>

        fun onChanged(type: NotificationSettingsItem, value: Boolean)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}