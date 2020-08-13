package com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface FilterMMLivestreamContract {
    interface ViewModel: BaseViewModel {
        val dataSource: List<String>

        fun apply()
        fun cancel()
    }

    interface Router {
        fun onBack()
    }
}