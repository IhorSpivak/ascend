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
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.doneit.ascend.presentation.models.group.PresentationGroupListModel
import com.doneit.ascend.presentation.models.group.toDTO
import com.doneit.ascend.presentation.utils.extensions.toGMTFormatter
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import java.util.*

@CreateFactory
@ViewModelDiModule
class MasterMindViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    private val formingRequestModel = PresentationGroupListModel()
    override val requestModel = MutableLiveData<GroupListDTO>(defaultRequest)

    private val _groups = requestModel.switchMap { groupUseCase.getGroupListPaged(viewModelScope, it) }
    private val user = userUseCase.getUserLive()

    override val groups = MediatorLiveData<GroupListWithUserPaged>()
    override val dataSource = List(INTERVALS_COUNT) {
        Date(it * TIME_INTERVAL.minutesToMills()).toDayTime()
    }

    init {
        groups.addSource(_groups) {
            updateListData(user.value, it)
        }

        groups.addSource(user) {
            updateListData(it, _groups.value)
        }
    }

    private fun Date.toDayTime(): String {
        val formatter = "h:mm aa".toGMTFormatter()
        return formatter.format(this)
    }

    private fun Int.minutesToMills(): Long {
        return this * 60 * 1000L
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

    override fun apply() {
        requestModel.postValue(formingRequestModel.toDTO(defaultRequest))
    }

    override fun cancel() {
        router.onBack()
    }

    override fun updateData() {
        requestModel.postValue(requestModel.value)
    }

    override fun onStartChatClick(groupId: Long, groupType: GroupType) {
        router.navigateToVideoChat(groupId, groupType)
    }

    override fun onGroupClick(groupId: Long) {
        router.navigateToGroupInfo(groupId)
    }

    override fun onFilterClick() {
        router.navigateToGroupsFilter()
    }

    companion object {
        val defaultRequest = GroupListDTO(
            perPage = 10,
            sortType = SortType.DESC,
            sortColumn = GroupEntity.START_TIME_KEY,
            groupType = GroupType.MASTER_MIND,
            groupStatus = GroupStatus.UPCOMING
        )

        private const val TIME_INTERVAL = 5//5 min
        private const val INTERVALS_COUNT = 24 * 60 / TIME_INTERVAL//1 day
    }
}