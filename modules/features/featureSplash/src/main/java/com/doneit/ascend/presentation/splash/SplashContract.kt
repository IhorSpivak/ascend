package com.doneit.ascend.presentation.splash

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.UserEntity

interface SplashContract {
    interface ViewModel {
        val user: LiveData<UserEntity?>
    }
}