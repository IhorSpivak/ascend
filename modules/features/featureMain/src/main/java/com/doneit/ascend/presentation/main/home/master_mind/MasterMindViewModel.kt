package com.doneit.ascend.presentation.main.home.master_mind

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.FilterModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class MasterMindViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    override val requestModel = MutableLiveData(defaultRequest)

    private val _groups = requestModel.switchMap { groupUseCase.getGroupListPaged(viewModelScope, it) }
    private val user = userUseCase.getUserLive()

    override val groups = MediatorLiveData<GroupListWithUserPaged>()
    override val filter: FilterModel
        get() {
            return FilterModel(
                requestModel.value?.daysOfWeen
                    .orEmpty()
                    .map { DayOfWeek.values()[it] }
                    .toMutableList(),
                System.currentTimeMillis(),//TODO
                System.currentTimeMillis()
            )
        }

    init {
        groups.addSource(_groups) {
            updateListData(user.value, it)
        }

        groups.addSource(user) {
            updateListData(it, _groups.value)
        }
    }

    private fun updateListData(user: UserEntity?, pagedList: PagedList<GroupEntity>?) {
        if (user != null && pagedList != null) {
            groups.postValue(
                GroupListWithUserPaged(
                    pagedList,
                    user
                )
            )
        }
    }

    override fun updateData() {
        requestModel.postValue(requestModel.value)
    }

    override fun updateRequestModel(requestModel: GroupListDTO) {
        this.requestModel.value = requestModel
    }

    override fun onStartChatClick(groupId: Long, groupType: GroupType) {
        router.navigateToVideoChat(groupId, groupType)
    }

    override fun onGroupClick(groupId: Long) {
        router.navigateToGroupInfo(groupId)
    }

    companion object {
        val defaultRequest = GroupListDTO(
            perPage = 10,
            sortType = SortType.DESC,
            sortColumn = GroupEntity.START_TIME_KEY,
            groupType = GroupType.MASTER_MIND,
            groupStatus = GroupStatus.UPCOMING
        )
    }
}