package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.create_group.add_member.AddMemberFragment
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerFragment
import com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker.WebinarCalendarPickerFragment
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupFragment
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerFragment
import com.doneit.ascend.presentation.main.create_group.date_picker.WebinarDatePickerFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarFragment
import com.doneit.ascend.presentation.main.create_group.meeting_count.NumberOfMeetingsFragment
import com.doneit.ascend.presentation.main.create_group.price_picker.PricePickerFragment
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.google.android.material.textfield.TextInputEditText
import com.vrgsoft.core.presentation.router.FragmentRouter

class LocalRouter(
    private val hostFragment: CreateGroupHostFragment
) : FragmentRouter(hostFragment.childFragmentManager),
    CreateGroupHostContract.LocalRouter {

    override val containerId = hostFragment.getContainerId()

    override fun onBack(): Boolean {
        return if(hostFragment.childFragmentManager.backStackEntryCount > 0) {
            hostFragment.childFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }

    override fun navigateToPricePicker(editor: TextInputEditText) {
        hostFragment.childFragmentManager.add(containerId,  PricePickerFragment(editor))
    }

    override fun navigateToCreateMMGroup(args: CreateGroupArgs, group: GroupEntity?, what: String?) {
        val fragment =
            CreateMMGroupFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
            if (group != null){
                putParcelable(what, group)
            }
        }

        hostFragment.childFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.simpleName)
            .setPrimaryNavigationFragment(fragment)
            .commit()//replace(containerId, fragment)
    }

    override fun navigateToCreateSupGroup(args: CreateGroupArgs, group: GroupEntity?, what: String?) {
        val fragment = CreateSupGroupFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
            if (group != null){
                putParcelable(what, group)
            }
        }

        hostFragment.childFragmentManager.replace(containerId, fragment)
    }

    override fun navigateToCreateWebinar(args: CreateGroupArgs, group: GroupEntity?, what: String?) {
        val fragment = CreateWebinarFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
            if (group != null){
                putParcelable(what, group)
            }
        }

        hostFragment.childFragmentManager.replace(containerId, fragment)
    }

    override fun navigateToCalendarPiker() {
        hostFragment.childFragmentManager.add(containerId,  CalendarPickerFragment())
    }

    override fun navigateToWebinarCalendarPiker(position: Int, group: GroupEntity?) {
        hostFragment.childFragmentManager.add(containerId,  WebinarCalendarPickerFragment(position, group))
    }

    override fun navigateToWebinarDatePiker() {
        hostFragment.childFragmentManager.add(containerId,  WebinarDatePickerFragment())
    }

    override fun navigateToDatePicker() {
        hostFragment.childFragmentManager.add(containerId, DatePickerFragment())
    }

    override fun navigateToMeetingCount(group: GroupEntity?, what: GroupAction?) {
        hostFragment.childFragmentManager.add(containerId,  NumberOfMeetingsFragment(group, what))    }

    override fun navigateToAddMember(groupType: GroupType) {
        hostFragment.childFragmentManager.add(containerId,  AddMemberFragment.getInstance(groupType))
    }
}