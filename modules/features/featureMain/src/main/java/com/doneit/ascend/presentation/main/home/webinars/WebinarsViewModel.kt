package com.doneit.ascend.presentation.main.home.webinars

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.groups.group_list.GroupListArgs
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
    private val masterMindUseCase: MasterMindUseCase,
    private val router: WebinarsContract.Router
): BaseViewModelImpl(), WebinarsContract.ViewModel {

    private lateinit var user: UserEntity

    private val groupListModel = MutableLiveData<GroupListDTO>()

    private val webinarFilterLiveData = MutableLiveData<HashMap<String, Boolean>>()
    private val webinarFilter = hashMapOf(
        WebinarFilter.RECOVERY.toString() to false,
        WebinarFilter.SUCCESS.toString() to false,
        WebinarFilter.FAMILY.toString() to false,
        WebinarFilter.SPIRITUAL.toString() to false
    )

    override val groups = groupListModel.switchMap {
        groupUseCase.getGroupListPaged(it).map {
            GroupListWithUserPaged(
                it,
                user!!
            )
        }
    }

    fun applyArguments(args: GroupListArgs) {
        viewModelScope.launch {
            var groupType = args.groupType

            if (groupType == GroupType.DAILY //according to requirement to display all created group on first tab,
                || groupType == GroupType.MY_GROUPS
            ) {
                groupType = null
            }

            val model = GroupListDTO(
                perPage = 50,
                sortType = args.sortType,
                sortColumn = GroupEntity.START_TIME_KEY,
                userId = args.userId,
                groupType = groupType,
                groupStatus = args.status,
                myGroups = args.isMyGroups
            )

            user = userUseCase.getUser()!!

            groupListModel.postValue(model)
        }
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model.id)
    }

    override fun onStartChatClick(groupId: Long) {
        router.navigateToVideoChat(groupId)
    }
}