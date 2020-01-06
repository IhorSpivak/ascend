package com.doneit.ascend.presentation.profile.notification_settings

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotificationSettingsContract {
    interface ViewModel: BaseViewModel {
        fun onChanged(id: Long)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}