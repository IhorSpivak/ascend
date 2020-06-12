package com.doneit.ascend.presentation.main.create_group.select_group_type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
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
            initTitles()
        }
    }

    private fun initTitles() {
        val titlePair = when (user?.community) {
            Community.FITNESS.title,
            Community.SPIRITUAL.title -> R.string.collaboration to R.string.group
            Community.RECOVERY.title -> R.string.group_title to R.string.workshop
            Community.FAMILY.title -> R.string.group_title to R.string.group
            Community.INDUSTRY.title -> R.string.collaboration to R.string.workshop
            else -> throw IllegalStateException("Unsupported community detected")
        }
        supportTitle.postValue(titlePair.first)
        createGroupTitle.postValue(titlePair.second)
    }

    override fun selectGroupType(type: GroupType) {
        router.navigateToCreateGroup(type)
    }

    override fun backClick() {
        router.onBack()
    }
}