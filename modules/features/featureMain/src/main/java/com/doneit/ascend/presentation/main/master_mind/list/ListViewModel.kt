package com.doneit.ascend.presentation.main.master_mind.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ListViewModel(
    private val masterMindUseCase: MasterMindUseCase,
    private val router: ListContract.Router
) : BaseViewModelImpl(), ListContract.ViewModel {

    override val masterMinds = MutableLiveData<PagedList<MasterMindEntity>>()
    private var isFollowed: Boolean? = false

    override fun updateData() {
        loadMasterMind(isFollowed)
    }

    override fun applyArguments(args: ListArgs) {
        isFollowed = args.isFollowed
        loadMasterMind(isFollowed)
    }

    private fun loadMasterMind(isFollowed: Boolean?) {
        viewModelScope.launch {
            val masterMinds = masterMindUseCase.getMasterMindList(isFollowed)
            this@ListViewModel.masterMinds.postValue(masterMinds)
        }
    }


    override fun openProfile(model: MasterMindEntity) {
        router.openProfile(model)
    }

    override fun openGroupList(id: Long) {
        router.navigateToGroupList(id)
    }
}