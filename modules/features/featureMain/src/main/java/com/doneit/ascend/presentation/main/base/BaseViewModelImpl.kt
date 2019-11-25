package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doneit.ascend.presentation.main.models.PresentationMessage

abstract class BaseViewModelImpl: ViewModel(), BaseViewModel {

    override val errorMessage =  MutableLiveData<PresentationMessage>()
    override val successMessage =  MutableLiveData<PresentationMessage>()
}