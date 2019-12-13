package com.doneit.ascend.presentation.main.search

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class SearchViewModel(
    private val router: SearchContract.Router
): BaseViewModelImpl(), SearchContract.ViewModel {

    override fun goBack() {
        router.closeActivity()
    }
}