package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface HomeContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>
    }

    interface Router
}