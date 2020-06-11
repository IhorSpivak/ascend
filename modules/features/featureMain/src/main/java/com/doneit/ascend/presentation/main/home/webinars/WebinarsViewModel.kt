package com.doneit.ascend.presentation.main.home.webinars

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.home.webinars.common.WebinarFilter
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch
import java.util.*

@CreateFactory
@ViewModelDiModule
class WebinarsViewModel (
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val router: WebinarsContract.Router
): BaseViewModelImpl(), WebinarsContract.ViewModel {

    private lateinit var user: UserEntity

    override val isRefreshing = MutableLiveData<Boolean>()
    override val userLiveData = liveData {
        emit(userUseCase.getUser()!!)
    }

    private val groupListModel = MutableLiveData<GroupListDTO>()

    private val webinarFilterLiveData = MutableLiveData<HashMap<String, Boolean>>()

    private val webinarFilter = hashMapOf(
        WebinarFilter.RECOVERY.toString() to false,
        WebinarFilter.FITNESS.toString() to false,
        WebinarFilter.INDUSTRY.toString() to false,
        WebinarFilter.FAMILY.toString() to false,
        WebinarFilter.SPIRITUAL.toString() to false
    )

    override val groups = groupListModel.switchMap {
        groupUseCase.getGroupListPaged(it).map {
            GroupListWithUserPaged(
                it,
                user
            )
        }
    }

    override fun updateFilter(filter: WebinarFilter) {
        viewModelScope.launch {
            isRefreshing.postValue(true)
            val model = GroupListDTO(
                perPage = 50,
                sortType = SortType.ASC,
                sortColumn = null,
                userId = null,
                groupType = GroupType.WEBINAR,
                groupStatus = null,
                myGroups = null,
                community = filter.toString()
            )
            groupListModel.postValue(model)
            webinarFilterLiveData.postValue(webinarFilter)
            isRefreshing.postValue(false)
        }
    }

    override fun checkUser(user: UserEntity) {
        this.user = user
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model.id)
    }

    override fun onStartChatClick(groupId: Long, groupType: GroupType) {
        router.navigateToVideoChat(groupId, groupType)
    }
}