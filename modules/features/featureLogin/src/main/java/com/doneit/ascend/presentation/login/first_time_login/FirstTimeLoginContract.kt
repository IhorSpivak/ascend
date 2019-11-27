package com.doneit.ascend.presentation.login.first_time_login

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.presentation.login.first_time_login.common.FirstTimeLoginArgs
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel

interface FirstTimeLoginContract {
    interface ViewModel : ArgumentedViewModel<FirstTimeLoginArgs>, QuestionStateListener {
        val canComplete: LiveData<Boolean>
        val questions: LiveData<List<QuestionEntity>>

        fun completeClick()
    }

    interface Router {
        fun goToMain()
    }
}