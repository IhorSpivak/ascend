package com.doneit.ascend.presentation.main.master_mind

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MasterMindContract {
    interface ViewModel: BaseViewModel {
        val masterMinds: LiveData<PagedList<MasterMindEntity>>

        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}