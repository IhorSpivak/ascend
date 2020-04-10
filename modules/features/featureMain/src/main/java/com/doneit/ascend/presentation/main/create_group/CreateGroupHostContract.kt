package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.create_group.add_member.common.AddMemberViewModel
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker.WebinarCalendarPickerContact
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupContract
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.create_group.date_picker.WebinarDatePickerContract
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupContract
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.create_group.meeting_count.NumberOfMeetingsContract
import com.doneit.ascend.presentation.main.create_group.price_picker.PricePickerContract
import com.doneit.ascend.presentation.models.GroupType
import com.google.android.material.textfield.TextInputEditText

interface CreateGroupHostContract {
    interface ViewModel : CreateGroupContract.ViewModel,
        CreateSupGroupContract.ViewModel,
        CalendarPickerContract.ViewModel,
        DatePickerContract.ViewModel,
        CreateMMGroupContract.ViewModel,
        IndividualGroupContract.ViewModel,
        CreateWebinarContract.ViewModel,
        WebinarCalendarPickerContact.ViewModel,
        AddMemberViewModel,
        NumberOfMeetingsContract.ViewModel,
        PricePickerContract.ViewModel,
        WebinarDatePickerContract.ViewModel{

        fun handleBaseNavigation(args: CreateGroupArgs, group: GroupEntity?, what: String?)
    }

    interface Router {
        fun onBack()
        fun navigateToDetails(group: GroupEntity)
        fun navigateToDetailsNoBackStack(group: GroupEntity)
    }

    interface LocalRouter {
        fun navigateToCreateMMGroup(args: CreateGroupArgs, group: GroupEntity?, what: String?)
        fun navigateToCreateSupGroup(args: CreateGroupArgs, group: GroupEntity?, what: String?)
        fun navigateToCreateWebinar(args: CreateGroupArgs, group: GroupEntity?, what: String?)
        fun navigateToCalendarPiker()
        fun navigateToWebinarCalendarPiker(position: Int)
        fun navigateToWebinarDatePiker()
        fun navigateToDatePicker()
        fun navigateToMeetingCount()
        fun navigateToAddMember(groupType: GroupType)
        fun onBack(): Boolean
        fun navigateToPricePicker(editor: TextInputEditText)
    }
}