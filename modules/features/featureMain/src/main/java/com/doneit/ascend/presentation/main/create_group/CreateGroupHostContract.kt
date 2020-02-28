package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupContract
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupContract

interface CreateGroupHostContract {
    interface ViewModel : CreateGroupContract.ViewModel,
        CreateSupGroupContract.ViewModel,
        CalendarPickerContract.ViewModel,
        DatePickerContract.ViewModel,
        CreateMMGroupContract.ViewModel,
        IndividualGroupContract.ViewModel {

        fun handleBaseNavigation(args: CreateGroupArgs)
    }

    interface Router {
        fun onBack()
    }

    interface LocalRouter {
        fun navigateToCreateMMGroup(args: CreateGroupArgs)
        fun navigateToCreateSupGroup(args: CreateGroupArgs)
        fun navigateToCalendarPiker()
        fun navigateToDatePicker()
        fun onBack(): Boolean
    }
}