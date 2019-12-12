package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val masterMindUseCase: MasterMindUseCase,
    private val groupUseCase: GroupUseCase,
    private val router: HomeContract.Router
) : BaseViewModelImpl(), HomeContract.ViewModel {

    override val user = userUseCase.getUserLive()
    override val groups = MutableLiveData<List<GroupEntity>>()
    override val masterMinds = MutableLiveData<List<MasterMindEntity>>()

    override fun navigateToGroupList() {
        router.navigateToGroupList()
    }

    override val isRefreshing = MutableLiveData<Boolean>()

    override fun updateData() {
        viewModelScope.launch {
            isRefreshing.postValue(true)
            val task1 = launch { updateGroups() }
            val task2 = launch { updateMasterMinds() }

            task1.join()
            task2.join()
            isRefreshing.postValue(false)
        }
    }

    private suspend fun updateGroups() {
        val responseEntity = groupUseCase.getDefaultGroupList()

        if (responseEntity.isSuccessful) {
            groups.postValue(responseEntity.successModel!!)
        }
    }

    private suspend fun updateMasterMinds() {
        val responseEntity = masterMindUseCase.getDafaultMasterMindList()

        if (responseEntity.isSuccessful) {
            masterMinds.postValue(responseEntity.successModel!!)
        }
    }
}