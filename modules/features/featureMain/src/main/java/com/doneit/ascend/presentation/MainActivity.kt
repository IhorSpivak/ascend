package com.doneit.ascend.presentation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.dialog.PermissionsRequiredDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import kotlinx.android.synthetic.main.activity_main.*
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

        bind<CalendarPickerUtil>() with provider {
            CalendarPickerUtil(
                instance()
            )
        }

        bind<MainContract.Router>() with provider { instance<MainRouter>() }

        bind<ViewModel>(ProfileViewModel::class.java.simpleName) with provider {
            ProfileViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<ViewModel>(MainViewModel::class.java.simpleName) with provider {
            MainViewModel(
                instance(),
                instance()
            )
        }

        bind<MainContract.ViewModel>() with provider { vm<MainViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container
    fun getContainerIdFull() = R.id.container_full

    private val viewModel: MainContract.ViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.onHomeClick()

        fabCreateGroup.setOnClickListener {
            viewModel.onCreateGroupClick()
        }

        setBottomNavigationListeners()
    }

    private fun setBottomNavigationListeners() {
        mainBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    viewModel.onHomeClick()
                    true
                }
                R.id.my_content -> {
                    viewModel.navigateToMyContent()
                    true
                }
                R.id.ascension_plan -> {
                    viewModel.navigateToAscensionPlan()
                    true
                }
                R.id.profile -> {
                    viewModel.navigateToProfile()
                    true
                }
                else -> false
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            VideoChatActivity.RESULT_CODE -> {
                val result =
                    data!!.extras!!.get(VideoChatActivity.RESULT_TAG) as VideoChatActivity.ResultStatus
                when (result) {
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