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
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), HomeContract.ViewModel {

    override val user = userUseCase.getUser()
    override val groups = MutableLiveData<List<GroupEntity>>()
    override val masterMinds = MutableLiveData<List<MasterMindEntity>>()

    override fun updateData() {
        viewModelScope.launch {
            launch { updateGroups() }
            launch { updateMasterMinds() }
        }
    }

    private suspend fun updateGroups() {
        val responseEntity = groupUseCase.getDefaultGroupList()

        if(responseEntity.isSuccessful) {
            groups.postValue(responseEntity.successModel!!)
        }
    }

    private suspend fun updateMasterMinds() {
        val responseEntity = masterMindUseCase.getDafaultMasterMindList()

        if(responseEntity.isSuccessful) {
            masterMinds.postValue(responseEntity.successModel!!)
        }
    }
}