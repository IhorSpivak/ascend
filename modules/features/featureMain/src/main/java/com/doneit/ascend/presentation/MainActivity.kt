package com.doneit.ascend.presentation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.dialog.PermissionsRequiredDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.common.BottomNavigationAdapter
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("MainActivity") {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<MainRouter>() with provider {
            MainRouter(
                this@MainActivity,
                instance()
            )
        }

        bind<ViewModel>(ProfileViewModel::class.java.simpleName) with provider {
            ProfileViewModel(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }

    private val router: MainRouter by instance()
    private val userUseCase: UserUseCase by instance()

    fun getContainerId() = R.id.container
    fun getContainerIdFull() = R.id.container_full

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationListener()

        fabCreateGroup.setOnClickListener {
            router.navigateToCreateGroup()
        }

        router.navigateToHome()

        GlobalScope.launch(Dispatchers.Main) {
            val user = userUseCase.getUserLive()

            user.observe(this@MainActivity, Observer {
                setNavigationListener(it?.role)
                setCreateGroupState(it?.isMasterMind ?: false)
            })
        }
    }

    private fun setNavigationListener(userRole: String? = null) {
        with(BottomNavigationAdapter(router, userRole)) {
            attach(mainBottomNavigationView)
        }
    }

    private fun setCreateGroupState(isEnabled: Boolean) {
        fabCreateGroup.isEnabled = isEnabled

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            VideoChatActivity.RESULT_CODE -> {
                val result = data!!.extras!!.get(VideoChatActivity.RESULT_TAG) as VideoChatActivity.ResultStatus
                when(result) {
                    VideoChatActivity.ResultStatus.POPUP_REQUIRED -> {
                        PermissionsRequiredDialog.create(this).show()
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        const val HOME_VM_TAG = "HOME_VM_TAG"
    }
}