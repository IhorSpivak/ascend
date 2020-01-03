package com.doneit.ascend.presentation.main.master_mind

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class MasterMindViewModel(
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    override fun goBack() {
        router.onBack()
    }

    override fun onSearchClick() {
        router.navigateToSearch()
    }
}