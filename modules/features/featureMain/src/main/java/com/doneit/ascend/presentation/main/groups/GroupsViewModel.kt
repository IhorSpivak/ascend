package com.doneit.ascend.presentation.main.groups

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class GroupsViewModel(
    private val router: GroupsContract.Router
) : BaseViewModelImpl(), GroupsContract.ViewModel {
    override fun backClick() {
        router.onBack()
    }
}