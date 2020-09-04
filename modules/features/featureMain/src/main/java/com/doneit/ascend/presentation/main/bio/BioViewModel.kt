package com.doneit.ascend.presentation.main.bio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.home.HomeContract
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class BioViewModel(
    private val masterMindUseCase: MasterMindUseCase,
    private val router: BioContract.Router
) : BaseViewModelImpl(), BioContract.ViewModel {
    override val user = MutableLiveData<UserEntity>()
    override val myRating = MutableLiveData<Int?>()


    override fun setRating(rating: Int) {
        user.value?.let {
            viewModelScope.launch {
                masterMindUseCase.setRating(it.id, rating)
            }
        }
    }


    override fun setUser(user: UserEntity) {
        this.user.postValue(user)
    }
}