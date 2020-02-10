package com.doneit.ascend.presentation.main.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotificationContract {
    interface ViewModel : BaseViewModel {
        val notifications: LiveData<PagedList<NotificationEntity>>

        fun goBack()
        fun onNotificationClick(id: Long)
        fun onDelete(id: Long)
    }

    interface Router {
        fun onBack()
        fun navigateToGroupInfo(id: Long)
    }
}