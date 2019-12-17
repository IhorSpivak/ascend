package com.doneit.ascend.presentation.main.master_mind.list

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ListViewModel(
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), ListContract.ViewModel {

    override val masterMinds = MutableLiveData<PagedList<MasterMindEntity>>()

    override fun applyArguments(args: ListArgs) {

        /*GlobalScope.launch {
            val masterMinds = masterMindUseCase.getMasterMindList(args.isFollowed)

            if (masterMinds.isSuccessful) {
                this@ListViewModel.masterMinds.postValue(masterMinds.successModel)
            }
        }*/
    }
}