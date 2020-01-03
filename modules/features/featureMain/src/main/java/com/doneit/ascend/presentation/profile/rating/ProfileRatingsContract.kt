package com.doneit.ascend.presentation.profile.rating

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ProfileRatingsContract {
    interface ViewModel: BaseViewModel {
        val rate: LiveData<Float>
        val ratings: LiveData<PagedList<RateEntity>>

        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}