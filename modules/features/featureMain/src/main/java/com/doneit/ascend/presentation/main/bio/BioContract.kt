package com.doneit.ascend.presentation.main.bio

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface BioContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>

        val myRating: LiveData<Int?>

        fun setRating(rating: Int)
        fun setUser(user: UserEntity)
    }

    interface Router
}