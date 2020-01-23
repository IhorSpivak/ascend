package com.doneit.ascend.presentation.main.groups.group_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.GroupListWithUserPaged
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupListViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: GroupListContract.Router
) : BaseViewModelImpl(), GroupListContract.ViewModel {

    override val groups = MutableLiveData<GroupListWithUserPaged>()

    override fun applyArguments(args: GroupListArgs) {
        viewModelScope.launch {
            var groupType = args.groupType

            if (groupType == GroupType.DAILY //according to requirement to display all created group on first tab,
                || groupType == GroupType.MY_GROUPS
            ) {
                groupType = null
            }

            val model = GroupListModel(
                perPage = 50,
                sortType = args.sortType,
                sortColumn = GroupEntity.START_TIME_KEY,
                userId = args.userId,
                groupType = groupType,
                groupStatus = args.status,
                myGroups = args.isMyGroups
            )

            val result = groupUseCase.getGroupListPaged(model)

            val user = userUseCase.getUser()
            groups.postValue(
                GroupListWithUserPaged(
                    result,
                    user!!
                )
            )
        }
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model)
    }

    override fun onStartChatClick(groupId: Long) {
        router.navigateToVideoChat(groupId)
    }
}