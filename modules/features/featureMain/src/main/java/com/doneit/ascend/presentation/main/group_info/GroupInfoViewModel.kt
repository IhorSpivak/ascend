package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupDetailsEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupInfoViewModel(
    private val router: GroupInfoContract.Router,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), GroupInfoContract.ViewModel {

    override val group = MutableLiveData<GroupDetailsEntity>()

    override fun loadData(groupId: Long) {
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(groupId)

            if (response.isSuccessful) {
                group.postValue(response.successModel)
            }
        }
    }

    override fun join() {
    }

    override fun subscribe() {
    }

    override fun deleteGroup() {
        GlobalScope.launch {
            groupUseCase.deleteGroup(group.value?.id ?: return@launch)
        }
    }

    override fun onBackPressed() {
        router.closeActivity()
    }
}