package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages

abstract class BaseViewModelImpl: ViewModel(), BaseViewModel {

    override val errorMessage =  MutableLiveData<PresentationMessage>()
    override val successMessage =  MutableLiveData<PresentationMessage>()

    protected fun showDefaultErrorMessage(message: String) {
        errorMessage.postValue(
            PresentationMessage(
                Messages.DEFAULT_ERROR.getId(),
                null,
                message
            )
        )
    }
}