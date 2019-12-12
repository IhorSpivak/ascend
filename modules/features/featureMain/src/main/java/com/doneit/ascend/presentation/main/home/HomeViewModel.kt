package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.groups.GroupsContract
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val masterMindUseCase: MasterMindUseCase,
    private val router: HomeContract.Router
) : BaseViewModelImpl(), HomeContract.ViewModel, GroupsContract.ViewModel {

    override val user = userUseCase.getUser()
    override val masterMinds = MutableLiveData<List<MasterMindEntity>>()
    override val isRefreshing = MutableLiveData<Boolean>()
    override val groups = MutableLiveData<List<GroupEntity>>()
    private  var groupType: GroupType? = null

    override fun applyArguments(args: GroupsArgs) {
        groupType = GroupType.values()[args.groupType]
    }

    override fun navigateToGroupList() {
        router.navigateToGroupList()
    }

    override fun updateData() {
        viewModelScope.launch {
            isRefreshing.postValue(true)
            val task1 = launch { fetchGroups() }
            val task2 = launch { fetchMasterMinds() }

            task1.join()
            task2.join()
            isRefreshing.postValue(false)
        }
    }

    override fun updateMasterMinds() {
        viewModelScope.launch {
            fetchMasterMinds()
        }
    }

    override fun updateGroups() {
        viewModelScope.launch {
            fetchGroups()
        }
    }

    private suspend fun fetchGroups() {
        val model = GroupListModel(
            perPage = 10,
            sortType = SortType.DESC,
            groupType = groupType
        )

        val responseEntity = groupUseCase.getGroupList(model)

        if (responseEntity.isSuccessful) {
            groups.postValue(responseEntity.successModel!!)
        }
    }

    private suspend fun fetchMasterMinds() {
        val responseEntity = masterMindUseCase.getDafaultMasterMindList()

        if (responseEntity.isSuccessful) {
            masterMinds.postValue(responseEntity.successModel!!)
        }
    }
}