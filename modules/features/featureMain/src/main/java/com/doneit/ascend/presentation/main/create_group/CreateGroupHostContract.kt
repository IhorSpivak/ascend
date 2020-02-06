package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupContract
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupContract

interface CreateGroupHostContract {
    interface ViewModel : CreateGroupContract.ViewModel,
        CreateSupGroupContract.ViewModel,
        CalendarPickerContract.ViewModel,
        DatePickerContract.ViewModel {

        fun handleBaseNavigation(args: CreateGroupArgs)
    }

    interface Router {
        fun onBack()
    }

    interface LocalRouter {
        fun navigateToCreateGroup(args: CreateGroupArgs)
        fun navigateToCreateSubGroup(args: CreateGroupArgs)
        fun navigateToCalendarPiker()
        fun navigateToDatePicker()
        fun onBack(): Boolean
    }
}