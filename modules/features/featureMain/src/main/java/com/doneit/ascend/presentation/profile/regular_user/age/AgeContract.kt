package com.doneit.ascend.presentation.profile.regular_user.age

import com.doneit.ascend.presentation.main.base.BaseViewModel
import java.util.*

interface AgeContract {
    interface ViewModel: BaseViewModel {
        fun setBirthday(date: Date)
        fun goBack()
    }
}