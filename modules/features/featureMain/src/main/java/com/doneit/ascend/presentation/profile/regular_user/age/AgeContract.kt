package com.doneit.ascend.presentation.profile.regular_user.age

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import java.util.*

interface AgeContract {
    interface ViewModel: BaseViewModel {
        val canSave: LiveData<Boolean>
        val birthdaySelected: LiveData<Date?>

        fun onBirthdaySelected(year: Int, month: MonthEntity, day: Int)
        fun saveBirthday()
        fun goBack()
    }
}