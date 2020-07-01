package com.doneit.ascend.presentation.main.create_group.select_group_type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.convertCommunityToResId
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class SelectGroupTypeViewModel(
    private val userUseCase: UserUseCase,
    private val router: SelectGroupTypeContract.Router
) : BaseViewModelImpl(), SelectGroupTypeContract.ViewModel {

    private var user: UserEntity? = null
    override val createGroupTitle = MutableLiveData<Int>()
    override val supportTitle = MutableLiveData<Int>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            user = userUseCase.getUser()
            createGroupTitle.postValue(
                convertCommunityToResId(
                    user?.community.orEmpty(),
                    GroupType.MASTER_MIND
                )
            )
            supportTitle.postValue(
                convertCommunityToResId(
                    user?.community.orEmpty(),
                    GroupType.SUPPORT
                )
            )
        }
    }

    override fun selectGroupType(type: GroupType) {
        router.navigateToCreateGroup(type)
    }

    override fun backClick() {
        router.onBack()
    }
}