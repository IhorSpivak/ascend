package com.doneit.ascend.presentation.main.groups

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import com.doneit.ascend.presentation.main.model.GroupListWithUser
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupsViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), GroupsContract.ViewModel {

    override val groups = MutableLiveData<GroupListWithUser>()

    override fun applyArguments(args: GroupsArgs) {
        GlobalScope.launch {

            val model = GroupListModel(
                perPage = 10,
                sortType = SortType.DESC,
                groupType = GroupType.values()[args.groupType]
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