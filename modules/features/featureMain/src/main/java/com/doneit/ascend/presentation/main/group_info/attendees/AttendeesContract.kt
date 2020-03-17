package com.doneit.ascend.presentation.main.group_info.attendees

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AttendeesContract {
    interface ViewModel: BaseViewModel {
        fun submitRequest(query: String)
        fun goBack()
        fun onClearClick()
        fun onAttendeeClick()
    }

    interface Router{
        fun onBack()
    }
}