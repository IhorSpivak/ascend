package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.toStringValueUI
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.doneit.ascend.presentation.main.home.group.GroupsContract
import com.doneit.ascend.presentation.main.home.group.common.GroupsArgs
import com.doneit.ascend.presentation.models.GroupListWithUser
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val masterMindUseCase: MasterMindUseCase,
    private val router: HomeContract.Router
) : BaseViewModelImpl(), HomeContract.ViewModel, GroupsContract.ViewModel {

    override val user = userUseCase.getUserLive()
    override val groups = MutableLiveData<GroupListWithUser>()
    override val masterMinds = MutableLiveData<List<MasterMindEntity>>()
    override val tabAdapter = MutableLiveData<TabAdapter>()

    override val isRefreshing = MutableLiveData<Boolean>()
    override val groupName = MutableLiveData<String>()
    private var groupType: GroupType? = null
    private var isMyGroups: Boolean? = null

    override fun applyArguments(args: GroupsArgs) {
        groupType = args.groupType
        groupName.postValue(groupType!!.toStringValueUI())

        isMyGroups = args.isMineGroups
    }

    override fun navigateToGroupList() {
        router.navigateToGroupList(null, groupType, isMyGroups)
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
            sortType = SortType.ASC,
            sortColumn = GroupEntity.START_TIME_KEY,
            groupType = if(groupType == GroupType.DAILY) null else groupType,//according to requirement to display all created group on first tab
            myGroups = isMyGroups
        )

        val responseEntity = groupUseCase.getGroupList(model)

        if (responseEntity.isSuccessful) {
            if (responseEntity.isSuccessful) {

                val user = userUseCase.getUser()

                groups.postValue(
                    GroupListWithUser(
                        responseEntity.successModel,
                        user!!
                    )
                )
            }
        }
    }

    override fun onSearchClick() {
        router.navigateToSearch()
    }

    override fun onAllMasterMindsClick() {
        router.navigateToAllMasterMinds()
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model.id)
    }

    override fun onStartChatClick(groupId: Long) {
        router.navigateToVideoChat(groupId)
    }

    private suspend fun fetchMasterMinds() {
        val responseEntity = masterMindUseCase.getDefaultMasterMindList()

        if (responseEntity.isSuccessful) {
            masterMinds.postValue(responseEntity.successModel!!)
        }
    }

    override fun openProfile(model: MasterMindEntity) {
        router.navigateToMMInfo(model)
    }

    override fun onNotificationClick() {
        router.navigateToNotifications()
    }
}