package com.doneit.ascend.presentation.main.master_mind_profile

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MMProfileContract {
    interface ViewModel: BaseViewModel {
        val profile : LiveData<MasterMindEntity>

        fun loadData(id: Long)
        fun report(content: String)
        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}