package com.doneit.ascend.presentation.main.group_list

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.dto.toStringValueUI
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.model.GroupListWithUser
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupListViewModel(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: GroupListContract.Router
) : BaseViewModelImpl(), GroupListContract.ViewModel {

    override val groups = MutableLiveData<GroupListWithUser>()
    override val groupType = MutableLiveData<String>()

    override fun backClick() {
        router.onBack()
    }

    override fun applyArguments(args: GroupListArgs) {
        GlobalScope.launch {

            groupType.postValue(GroupType.values()[args.groupType].toStringValueUI())

            val model = GroupListModel(
                perPage = 50,
                sortType = SortType.DESC,
                groupType = GroupType.values()[args.groupType],
                myGroups = args.isMyGroups
            )

            val result = groupUseCase.getGroupList(model)

            if (result.isSuccessful) {

                val user = userUseCase.getUser()

                groups.postValue(
                    GroupListWithUser(
                        result.successModel,
                        user!!
                    )
                )
            }
        }
    }
}