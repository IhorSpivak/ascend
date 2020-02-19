package com.doneit.ascend.presentation.login.first_time_login

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface FirstTimeLoginContract {
    interface ViewModel : BaseViewModel, QuestionStateListener {
        val canComplete: LiveData<Boolean>
        val questions: LiveData<QuestionListEntity?>

        fun completeClick()
    }

    interface Router {
        fun goToMain()
    }
}