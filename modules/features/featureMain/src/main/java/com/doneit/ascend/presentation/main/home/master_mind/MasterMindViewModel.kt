package com.doneit.ascend.presentation.main.home.master_mind

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

@CreateFactory
@ViewModelDiModule
class MasterMindViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    private val requestModel = GroupListDTO(
        perPage = 10,
        sortType = SortType.DESC,
        sortColumn = GroupEntity.START_TIME_KEY,
        groupType = GroupType.MASTER_MIND,
        groupStatus = GroupStatus.UPCOMING
    )

    private val shouldUpdateData = SingleLiveEvent<Void>()
    private val _groups =
        shouldUpdateData.switchMap { groupUseCase.getGroupListPaged(requestModel) }
    private val user = userUseCase.getUserLive()

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

    override fun updateData() {
        shouldUpdateData.call()
    }

    override fun onStartChatClick(groupId: Long) {
        router.navigateToVideoChat(groupId)
    }

    override fun onGroupClick(groupId: Long) {
        router.navigateToGroupInfo(groupId)
    }
}