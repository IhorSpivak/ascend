package com.doneit.ascend.presentation.main.ascension_plan.create_goal

import androidx.databinding.ObservableField
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CreateGoalsContract {
    interface ViewModel : BaseViewModel {
        val counter: ObservableField<Int>

        fun onBackClick()
        fun plus()
        fun minus()
    }

    interface Router {
        fun onBack()
    }
}