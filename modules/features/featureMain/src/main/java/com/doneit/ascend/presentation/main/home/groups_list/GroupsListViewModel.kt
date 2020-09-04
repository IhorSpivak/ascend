package com.doneit.ascend.presentation.main.home.groups_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class GroupsListViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: GroupsListContract.Router
) : BaseViewModelImpl(), GroupsListContract.ViewModel {

    override val requestModel = MutableLiveData(defaultRequest)

    private val _groups = requestModel.switchMap { groupUseCase.getGroupListPaged(viewModelScope, it) }
    override val user = userUseCase.getUserLive()

    override val groups = MediatorLiveData<GroupListWithUserPaged>()

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

    override fun setGroupType(groupType: GroupType?) {
        requestModel.value = requestModel.value?.copy(groupType = groupType)
    }

    override fun updateData(userId: Long?) {
        requestModel.postValue(requestModel.value?.copy(userId = userId))
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
            sortColumn = GroupEntity.START_TIME_KEY
        )
    }
}