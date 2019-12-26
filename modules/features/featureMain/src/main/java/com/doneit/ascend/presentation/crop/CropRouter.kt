package com.doneit.ascend.presentation.crop

import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.vrgsoft.core.presentation.router.FragmentRouter

class CropRouter(
    private val activity: CropActivity
) : FragmentRouter(activity.supportFragmentManager) {

    override val containerId = activity.getContainerId()

    fun navigateToCrop(fragment: Fragment) {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }
}