package com.doneit.ascend.presentation.main.filter.tag_filter

import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class TagFilterViewModel(
    groupUseCase: GroupUseCase
) :BaseTagFilterViewModel<TagFilterModel>(groupUseCase), TagFilterContract.ViewModel {
    override fun initFilterModel(): TagFilterModel {
        return TagFilterModel()
    }
}