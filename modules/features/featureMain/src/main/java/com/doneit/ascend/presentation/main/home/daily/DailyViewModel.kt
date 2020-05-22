package com.doneit.ascend.presentation.main.home.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.group.GroupListWithUser
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class DailyViewModel (
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val masterMindUseCase: MasterMindUseCase,
    private val router: DailyContract.Router
): BaseViewModelImpl(), DailyContract.ViewModel {

    override val isRefreshing = MutableLiveData<Boolean>()
    override val groups = MutableLiveData<GroupListWithUser>()
    override val masterMinds = MutableLiveData<List<MasterMindEntity>>()

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

    override fun navigateToDailyGroups() {
        router.navigateToDailyGroupList(null, null, true)
    }

    override fun onGroupClick(groupId: Long) {
        router.navigateToGroupInfo(groupId)
    }

    override fun onStartChatClick(groupId: Long, groupType: GroupType) {
        router.navigateToVideoChat(groupId, groupType)
    }

    override fun onAllMasterMindsClick() {
        router.navigateToAllMasterMinds()
    }

    override fun openProfile(mmId: Long) {
        router.navigateToMMInfo(mmId)
    }

    private suspend fun fetchGroups() {
        val model = GroupListDTO(
            sortType = SortType.ASC,
            sortColumn = GroupEntity.START_TIME_KEY,
            myGroups = true
        )

        val responseEntity = groupUseCase.getGroupList(model)

        if (responseEntity.isSuccessful) {
            if (responseEntity.isSuccessful) {

                val user = userUseCase.getUser()

                groups.postValue(
                    GroupListWithUser(
                        responseEntity.successModel!!,
                        user!!
                    )
                )
            }
        }
    }

    private suspend fun fetchMasterMinds() {
        val responseEntity = masterMindUseCase.getDefaultMasterMindList()

        if (responseEntity.isSuccessful) {
            masterMinds.postValue(responseEntity.successModel!!)
        }
    }
}