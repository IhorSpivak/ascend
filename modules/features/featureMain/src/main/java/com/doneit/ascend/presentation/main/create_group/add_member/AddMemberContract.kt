package com.doneit.ascend.presentation.main.create_group.add_member

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddMemberContract {
    interface ViewModel: BaseViewModel {
        //fun submitRequest(query: String)
        fun goBack()
        fun onMemberClick()
    }
}