package com.doneit.ascend.presentation

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.dialog.PermissionsRequiredDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.ascension_plan.AscensionPlanFragment
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityMainBinding
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.home.master_mind.MasterMindContract
import com.doneit.ascend.presentation.main.home.master_mind.MasterMindViewModel
import com.doneit.ascend.presentation.main.home.master_mind.MasterMindViewModelFactory
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.profile.master_mind.MMProfileFragment
import com.doneit.ascend.presentation.profile.regular_user.UserProfileFragment
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainActivity : BaseActivity(), MainActivityListener {

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
                instance(),
                instance(),
                instance()
            )
        }

        bind<MainContract.ViewModel>() with provider { vm<MainViewModel>(instance()) }


        //todo fix wrong lifecycle for this model
        bind<ViewModelProvider.Factory>(tag = "MasterMindGroups") with singleton {
            MasterMindViewModelFactory(instance(), instance(), instance())
        }
        bind<MasterMindContract.ViewModel>() with provider {
            vm<MasterMindViewModel>(instance(tag = "MasterMindGroups"))
        }
    }

    fun getContainerId() = R.id.container
    fun getContainerIdFull() = R.id.container_full

    private val viewModel: MainContract.ViewModel by instance()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.model = viewModel
        super.onCreate(savedInstanceState)
        viewModel.onHomeClick()
        if (intent.extras?.containsKey(Constants.KEY_GROUP_ID) == true) {
            intent.extras?.get(Constants.KEY_GROUP_ID)?.let {
                viewModel.tryToNavigateToGroupInfo(it.toString().toLong())
            }
        }
        binding.fabCreateGroup.setOnClickListener {
            viewModel.onCreateGroupClick()
        }

        setBottomNavigationListeners()
        setBackStackHandler()
    }

    private fun setBackStackHandler() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.findFragmentById(getContainerIdFull()) == null) {
                binding.mainBottomNavigationView.selectedItemId =
                    when (supportFragmentManager.findFragmentById(getContainerId())) {
                        is HomeFragment -> R.id.home
                        is AscensionPlanFragment -> R.id.ascension_plan
                        is UserProfileFragment, is MMProfileFragment -> R.id.profile
                        else -> return@addOnBackStackChangedListener
                    }
            }
        }
    }

    private fun setBottomNavigationListeners() {
        binding.mainBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    viewModel.onHomeClick()
                    true
                }
                R.id.my_content -> {
                    viewModel.navigateToMyContent()
                    false //TODO change to true when fragment will be implemented
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

    override fun setTitle(title: String, isLogoVisible: Boolean) {
        binding.tvTitle.text = title
        binding.ascendLogo.visibleOrGone(isLogoVisible)
    }

    override fun setSearchEnabled(isVisible: Boolean) {
        binding.btnSearch.visible(isVisible)
    }

    override fun setFilterEnabled(isVisible: Boolean) {
        binding.btnFilter.visible(isVisible)
    }

    override fun setChatEnabled(isVisible: Boolean) {
        binding.btnChat.visible(isVisible)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.get(Constants.KEY_GROUP_ID)?.let {
            viewModel.tryToNavigateToGroupInfo(it as Long)
        }
    }

    companion object {
        const val HOME_VM_TAG = "HOME_VM_TAG"
    }
}