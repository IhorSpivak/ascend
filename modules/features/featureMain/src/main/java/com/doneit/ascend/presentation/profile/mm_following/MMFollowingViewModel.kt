package com.doneit.ascend.presentation.profile.mm_following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.group.toDTO
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class MMFollowingViewModel(
    private val masterMindUseCase: MasterMindUseCase,

    private val router: MMFollowingContract.Router
) : BaseViewModelImpl(), MMFollowingContract.ViewModel {

    private val refetch = MutableLiveData<Unit>()
    override val masterMinds = refetch.switchMap { masterMindUseCase.getMasterMindList(true) }

    override fun refetch() {
        refetch.postValue(Unit)
    }

    override fun unfollow(id: Long) {
        viewModelScope.launch {
            val result = masterMindUseCase.unfollow(id)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }



    override fun openInfo(entity: MasterMindEntity) {
        router.navigateToMMInfo(entity.id)
    }

    override fun onAddMasterMindClick() {
        router.navigateToAddMasterMind()
    }

    override fun onBackClick() {
        router.onBack()
    }
}