package com.doneit.ascend.presentation.main.home

import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface HomeContract {
    interface ViewModel : BaseViewModel {
        fun getUser() : UserEntity
    }

    interface Router
}