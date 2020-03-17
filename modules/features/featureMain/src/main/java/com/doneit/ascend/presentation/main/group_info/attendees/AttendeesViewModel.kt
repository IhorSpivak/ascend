package com.doneit.ascend.presentation.main.group_info.attendees

import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.search.SearchContract
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AttendeesViewModel (
    private val router: SearchContract.Router,
    private val attendeesUseCase: SearchUseCase
) : BaseViewModelImpl(), AttendeesContract.ViewModel {
    override fun submitRequest(query: String) {

    }

    override fun goBack() {
    }

    override fun onClearClick() {
    }

    override fun onAttendeeClick() {
    }

}