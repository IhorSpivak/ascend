package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.models.PresentationMessage

interface BaseViewModel : LifecycleObserver {
    val errorMessage: LiveData<PresentationMessage>
    val successMessage: LiveData<PresentationMessage>
}