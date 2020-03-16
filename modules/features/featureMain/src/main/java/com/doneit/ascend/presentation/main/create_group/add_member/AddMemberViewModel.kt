package com.doneit.ascend.presentation.main.create_group.add_member

import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.search.SearchContract
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AddMemberViewModel(
private val router: SearchContract.Router,
private val searchUseCase: SearchUseCase
) : BaseViewModelImpl(), AddMemberContract.ViewModel {

   /* override fun submitRequest(query: String) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    override fun goBack() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMemberClick() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}