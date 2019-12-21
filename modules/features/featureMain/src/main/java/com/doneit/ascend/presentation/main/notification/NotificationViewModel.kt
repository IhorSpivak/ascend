package com.doneit.ascend.presentation.main.notification

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val router: NotificationContract.Router,
    private val notificationUseCase: NotificationUseCase
) : BaseViewModelImpl(), NotificationContract.ViewModel {

    override val notifications = MutableLiveData<PagedList<NotificationEntity>>()

    init {
        GlobalScope.launch {
            val notificationsList = notificationUseCase.getNotificationList(
                NotificationListModel(
                    perPage = 10,
                    sortColumn = "created_at",
                    sortType = SortType.DESC
                )
            )
            notifications.postValue(notificationsList)
        }
    }

    override fun goBack() {
        router.closeActivity()
    }

    override fun onNotificationClick(id: Long) {

    }

    override fun onDelete(id: Long) {
        GlobalScope.launch {
            val response = notificationUseCase.delete(id)

            if (response.isSuccessful) {

            }
        }
    }
}