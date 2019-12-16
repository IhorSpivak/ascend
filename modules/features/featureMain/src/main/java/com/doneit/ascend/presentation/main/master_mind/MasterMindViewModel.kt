package com.doneit.ascend.presentation.main.master_mind

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class MasterMindViewModel(
    private val router: MasterMindContract.Router
) : BaseViewModelImpl(), MasterMindContract.ViewModel {

    override val masterMinds: LiveData<PagedList<MasterMindEntity>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun goBack() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}