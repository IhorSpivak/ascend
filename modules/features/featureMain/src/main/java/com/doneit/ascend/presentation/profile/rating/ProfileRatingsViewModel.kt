package com.doneit.ascend.presentation.profile.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.dto.RatingsModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ProfileRatingsViewModel (
    private val userUseCase: UserUseCase,
    private val router: ProfileRatingsContract.Router
): BaseViewModelImpl(), ProfileRatingsContract.ViewModel {

    override val rate = userUseCase.getUserLive().map { it!!.rating!! }
    override val ratings = MutableLiveData<PagedList<RateEntity>>()

    init {
        viewModelScope.launch {
            val list = userUseCase.getRates(RatingsModel())
            ratings.postValue(list)
        }
    }

    override fun onBackClick() {
        router.onBack()
    }
}