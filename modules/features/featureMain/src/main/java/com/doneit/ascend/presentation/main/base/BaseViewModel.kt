package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.LifecycleObserver
import com.doneit.ascend.presentation.models.PresentationMessage
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface BaseViewModel : LifecycleObserver {
    val errorMessage: SingleLiveManager<PresentationMessage>
    val successMessage: SingleLiveManager<PresentationMessage>
}