package com.doneit.ascend.presentation.main.home.groups

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.TagEntity
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
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupsViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val router: GroupsContract.Router
) : BaseViewModelImpl(), GroupsContract.ViewModel {
    private lateinit var user: UserEntity

    override val isRefreshing = MutableLiveData<Boolean>()
    override val userLiveData = liveData {
        emit(userUseCase.getUser()!!)
    }
    private val groupListModel = MutableLiveData<GroupListDTO>()

    override val groups = groupListModel.switchMap {
        groupUseCase.getGroupListPaged(it).map {
            GroupListWithUserPaged(
                it,
                user!!
            )
        }
    }
    override var tags = MutableLiveData<List<TagEntity>>()

    init {
        viewModelScope.launch {

            val response = groupUseCase.getTags()
            tags.postValue(
                if (response.isSuccessful) {
                    updateFilter(response.successModel!![0])
                    response.successModel!!

                } else {
                    listOf()
                }
            )
        }
    }

    private val groupFilterLiveData = MutableLiveData<TagEntity>()

    override fun checkUser(user: UserEntity) {
        this.user = user
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model.id)
    }

    override fun onStartChatClick(groupId: Long) {
        router.navigateToVideoChat(groupId)
    }

    override fun updateFilter(filter: TagEntity?) {
        viewModelScope.launch {
            isRefreshing.postValue(true)
            val model = GroupListDTO(
                perPage = 50,
                sortType = SortType.ASC,
                sortColumn = null,
                userId = null,
                groupType = GroupType.SUPPORT,
                groupStatus = null,
                myGroups = null,
                community = null,
                tagId = filter?.id
            )
            groupListModel.postValue(model)
            groupFilterLiveData.postValue(filter)
            isRefreshing.postValue(false)
        }
    }

}