package com.doneit.ascend.presentation.profile.notification_settings

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class NotificationSettingsViewModel (
    private val router: NotificationSettingsContract.Router
): BaseViewModelImpl(), NotificationSettingsContract.ViewModel {

    override fun onChanged(id: Long) {
        viewModelScope.launch {
            //todo make a request
        }
    }

    override fun onBackClick() {
        router.onBack()
    }
}