package com.vrgsoft.carButler.presentation.main

import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainRouter
) : FragmentRouter(activity.supportFragmentManager) {

    override val containerId = R.id.container//todo: change
}