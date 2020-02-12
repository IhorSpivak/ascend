package com.doneit.ascend.presentation.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class NotificationViewModel(
    private val router: NotificationContract.Router,
    private val notificationUseCase: NotificationUseCase
) : BaseViewModelImpl(), NotificationContract.ViewModel {

    override val notifications: LiveData<PagedList<NotificationEntity>>

    init {
        val requestModel = NotificationListDTO(
            perPage = 10,
            sortColumn = "created_at",
            sortType = SortType.DESC
        )

        notifications = notificationUseCase.getNotificationList(requestModel)
    }

    override fun goBack() {
        router.onBack()
    }

    override fun onNotificationClick(id: Long) {
        router.navigateToGroupInfo(id)
    }

    override fun onDelete(id: Long) {
        viewModelScope.launch {
            val response = notificationUseCase.delete(id)

            if(response.isSuccessful.not()) {
                showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
            }
        }
    }
}