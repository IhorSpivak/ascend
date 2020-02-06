package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerFragment
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupFragment
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupFragment
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
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

    override fun navigateToCreateGroup(args: CreateGroupArgs) {
        val fragment = CreateGroupFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
        }

        hostFragment.childFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.simpleName)
            .setPrimaryNavigationFragment(fragment)
            .commit()//replace(containerId, fragment)
    }

    override fun navigateToCreateSubGroup(args: CreateGroupArgs) {
        val fragment = CreateSupGroupFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
        }

        hostFragment.childFragmentManager.replace(containerId, fragment)
    }

    override fun navigateToCalendarPiker() {
        hostFragment.childFragmentManager.add(containerId,  CalendarPickerFragment())
    }

    override fun navigateToDatePicker() {
        hostFragment.childFragmentManager.add(containerId, DatePickerFragment())
    }

}