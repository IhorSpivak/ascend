package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.create_group.add_member.AddMemberContract
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker.CalendarWithTimePickerContact
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupContract
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract

interface CreateGroupHostContract {
    interface ViewModel : CreateGroupContract.ViewModel,
        CreateSupGroupContract.ViewModel,
        CalendarPickerContract.ViewModel,
        DatePickerContract.ViewModel,
        CreateMMGroupContract.ViewModel,
        IndividualGroupContract.ViewModel,
        CreateWebinarContract.ViewModel,
        CalendarWithTimePickerContact.ViewModel,
        AddMemberContract.ViewModel{

        fun handleBaseNavigation(args: CreateGroupArgs, group: GroupEntity?, what: String?)
        fun updateGroup(group: GroupEntity)
    }

    interface Router {
        fun onBack()
    }

    interface LocalRouter {
        fun navigateToCreateMMGroup(args: CreateGroupArgs, group: GroupEntity?, what: String?)
        fun navigateToCreateSupGroup(args: CreateGroupArgs)
        fun navigateToCreateWebinar(args: CreateGroupArgs)
        fun navigateToCalendarPiker()
        fun navigateToDatePicker()
        fun navigateToAddMember(individual: Boolean)
        fun onBack(): Boolean
    }
}