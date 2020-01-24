package com.doneit.ascend.presentation.profile.crop

import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.extensions.add
import com.vrgsoft.core.presentation.router.FragmentRouter

class CropRouter(
    private val activity: CropActivity
) : FragmentRouter(activity.supportFragmentManager) {

    override val containerId = activity.getContainerId()

    fun navigateToCrop(fragment: Fragment) {
        activity.supportFragmentManager.add(R.id.container, fragment)
    }

    fun goBack() {
        activity.finish()
    }
}