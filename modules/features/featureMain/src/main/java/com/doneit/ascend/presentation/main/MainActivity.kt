package com.doneit.ascend.presentation.main

import android.os.Bundle
import android.view.View
import com.doneit.ascend.presentation.main.MainRouter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.BottomNavigationAdapter
import com.doneit.ascend.presentation.main.common.ToolbarListener
import com.doneit.ascend.presentation.utils.Constants.TYPE_MASTER_MIND
import com.doneit.ascend.presentation.utils.LocalStorage
import com.vrgsoft.core.presentation.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MainActivity : BaseActivity(), ToolbarListener {

    override fun diModule() = Kodein.Module("MainActivity") {
        bind<MainRouter>() with provider {
            MainRouter(
                this@MainActivity,
                instance()
            )
        }
    }

    private val router: MainRouter by instance()
    private val localStorage: LocalStorage by instance()

    fun getContainerId() = R.id.container
    fun getFullContainerId() =
        R.id.fullContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationListener()

        btbBack.setOnClickListener {
            router.onBack()
        }

        fabCreateGroup.setOnClickListener {
            router.navigateToCreateGroup()
        }

        router.navigateToHome()

        val user = localStorage.loadUser()

        if (user.role == TYPE_MASTER_MIND) {
            setCreateGroupState(true)
        } else {
            setCreateGroupState(false)
        }
    }

    private fun setNavigationListener() {
        with(BottomNavigationAdapter(router)) {
            attach(mainBottomNavigationView)
        }
    }

    override fun backButtonChangeVisibility(isShow: Boolean) {
        btbBack.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun searchButtonChangeVisibility(isShow: Boolean) {
        btbSearch.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun notificationChangeVisibility(isShow: Boolean) {
        btbNotification.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showHideNotificationBadge(isShow: Boolean) {
//        BadgeUtils.attachBadgeDrawable(badgeDrawable, anchor, anchorFrameLayoutParent)
    }

    override fun setCreateGroupState(isEnabled: Boolean) {
        fabCreateGroup.isEnabled = isEnabled
    }

    override fun setTitle(text: String) {
        tvTitle.text = text
    }
}