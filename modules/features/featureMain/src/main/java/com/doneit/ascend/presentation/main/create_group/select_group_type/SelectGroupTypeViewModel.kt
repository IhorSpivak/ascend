package com.doneit.ascend.presentation.main.create_group.select_group_type

import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class SelectGroupTypeViewModel(
    private val router: SelectGroupTypeContract.Router
) : BaseViewModelImpl(), SelectGroupTypeContract.ViewModel {

    override fun selectGroupType(type: GroupType) {
        router.navigateToCreateGroup(type)
    }

    override fun backClick() {
        router.onBack()
    }
}