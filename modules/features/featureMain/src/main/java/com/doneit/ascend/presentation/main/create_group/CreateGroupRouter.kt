package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerFragment
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeContract
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeFragment
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerFragment
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.utils.extensions.replaceWithBackStack
import com.vrgsoft.core.presentation.router.FragmentRouter

class CreateGroupRouter(
    private val activity: CreateGroupActivity
) : FragmentRouter(activity.supportFragmentManager),
    CreateGroupContract.Router,
    SelectGroupTypeContract.Router {

    override val containerId = activity.getContainerId()

    override fun onBack() {
        if (activity.supportFragmentManager.backStackEntryCount == 0) {
            activity.finish()
        } else {
            activity.supportFragmentManager.popBackStack()
        }
    }

    override fun closeActivity() {
        activity.finish()
    }

    override fun navigateToCreateGroup(type: String) {
        val args = CreateGroupArgs(type)

        val fragment = CreateGroupFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(containerId, fragment)
    }

    fun navigateToSelectGroupType() {
        activity.supportFragmentManager.replace(containerId, SelectGroupTypeFragment())
    }

    override fun navigateToCalendarPiker() {
        activity.supportFragmentManager.add(containerId, CalendarPickerFragment())
    }

    override fun navigateToDatePicker() {
        activity.supportFragmentManager.add(containerId, DatePickerFragment())
    }
}