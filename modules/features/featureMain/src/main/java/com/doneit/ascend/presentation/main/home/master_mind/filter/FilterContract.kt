package com.doneit.ascend.presentation.main.home.master_mind.filter

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface FilterContract {
    interface ViewModel: BaseViewModel {
        val dataSource: List<String>
        val requestModel: LiveData<GroupListDTO>

        fun apply()
        fun cancel()
    }

    interface Router {
        fun onBack()
    }
}