package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface HomeContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>
        fun getListOfTitles(): List<Int>
    }

    interface Router
}