package com.doneit.ascend.presentation.main.master_mind.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class ListViewModel(
    private val masterMindUseCase: MasterMindUseCase,
    private val router: ListContract.Router
) : BaseViewModelImpl(), ListContract.ViewModel {

    private val followedLive = MutableLiveData<Boolean>()
    override val masterMinds = followedLive.switchMap {
        masterMindUseCase.getMasterMindList(it)
    }

    override fun updateData() {
        followedLive.postValue(followedLive.value)
    }

    override fun applyArguments(args: ListArgs) {
        followedLive.postValue(args.isFollowed)
    }

    override fun openProfile(item: MasterMindEntity) {
        router.navigateToMMInfo(item.id)
    }

    override fun openGroupList(item: MasterMindEntity) {
        router.navigateToGroupList(item.id, null, null, item.fullName)
    }
}