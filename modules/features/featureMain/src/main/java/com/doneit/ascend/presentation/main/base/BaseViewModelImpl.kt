package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.ViewModel
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

abstract class BaseViewModelImpl: ViewModel(), BaseViewModel {

    override val errorMessage =  SingleLiveManager<PresentationMessage>()
    override val successMessage =  SingleLiveManager<PresentationMessage>()
    override val progressDialog = SingleLiveManager<Boolean>()

    protected fun showDefaultErrorMessage(message: String) {
        errorMessage.call(
            PresentationMessage(
                Messages.DEFAULT_ERROR.getId(),
                null,
                message
            )
        )
    }

    protected fun showProgress(isProgressShown: Boolean) {
        progressDialog.call(isProgressShown)
    }
}