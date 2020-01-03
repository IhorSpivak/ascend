package com.doneit.ascend.presentation.profile.mm_following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class MMFollowingViewModel (
    private val masterMindUseCase: MasterMindUseCase,
    private val router: MMFollowingContract.Router
): BaseViewModelImpl(), MMFollowingContract.ViewModel {

    override val masterMinds = MutableLiveData<PagedList<MasterMindEntity>>()

    override fun fetchList() {
        updateData()
    }

    override fun unfollow(id: Long) {
        viewModelScope.launch {
            val result = masterMindUseCase.unfollow(id)

            if(result.isSuccessful) {
                updateData()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun onAddMasterMindClick() {
        router.navigateToAddMasterMind()
    }

    override fun onBackClick() {
        router.goBack()
    }

    private fun updateData() {
        viewModelScope.launch {
            val result = masterMindUseCase.getMasterMindList(true)
            masterMinds.postValue(result)
        }
    }
}