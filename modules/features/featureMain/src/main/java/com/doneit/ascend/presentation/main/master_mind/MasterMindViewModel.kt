package com.doneit.ascend.presentation.main.master_mind

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class MasterMindViewModel(
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    override fun goBack() {
        router.closeActivity()
    }

    override fun onSearchClick() {
        router.navigateToSearch()
    }
}