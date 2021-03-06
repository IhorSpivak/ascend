package com.doneit.ascend.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.common.CommunityArrayAdapter
import com.doneit.ascend.presentation.dialog.PermissionsRequiredDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ActivityMainBinding
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedFragment
import com.doneit.ascend.presentation.models.PresentationCommunityModel
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.profile.master_mind.MMProfileFragment
import com.doneit.ascend.presentation.profile.regular_user.UserProfileFragment
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.*
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.*

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
                instance(),
                instance(),
                instance()
            )
        }

        bind<MainContract.ViewModel>() with provider { vm<MainViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container
    fun getContainerIdFull() = R.id.container_full

    private val viewModel: MainContract.ViewModel by instance()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        fun getExtra(key: String, action: (Long) -> Unit) {
            intent?.extras?.getLong(key)?.let {
                if (it > 0) action(it)
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.model = viewModel
        super.onCreate(savedInstanceState)
        parseIntent(intent)
        if (resources.getBoolean(R.bool.portrait_only)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        viewModel.onHomeClick()

        getExtra(Constants.KEY_GROUP_ID) { viewModel.tryToNavigateToGroupInfo(it) }
        getExtra(Constants.KEY_PROFILE_ID) { viewModel.tryToNavigateToProfile(it) }

        binding.fabCreateGroup.setOnClickListener {
            viewModel.onCreateGroupClick()
        }
        viewModel.userShare.observe(this, Observer {
            shareTo(Constants.DEEP_LINK_PROFILE_URL + it.id)
        })
        viewModel.communities.observe(this, Observer { communities ->
            val adapter = CommunityArrayAdapter(this, binding.communityDropDown, communities)

            initSpinner(
                binding.communityDropDown,
                communityListener,
                adapter,
                communities.indexOfFirst { it.isSelected }
            )
        })

        setBottomNavigationListeners()
        setBackStackHandler()
    }


    override fun onResume() {
        super.onResume()
        hideKeyboard()

    }

    private fun parseIntent(intent: Intent?) {
        intent ?: return
        if (intent.scheme == Constants.DEEP_LINK_APP_SCHEME) {
            intent.data?.let {
                fun getIdFromPath(): Long? {
                    return it.path
                        ?.substring(1)
                        ?.substringBefore('/')
                        ?.toLongOrNull()
                }
                when {
                    it.path.orEmpty().endsWith(Constants.DEEP_LINK_START_SUFFIX) -> {
                        viewModel.tryToNavigateToGroupInfo(getIdFromPath() ?: return)
                    }
                    it.path.orEmpty().endsWith(Constants.DEEP_LINK_JOIN_SUFFIX) -> {
                        viewModel.tryToNavigateToGroupInfo(getIdFromPath() ?: return)
                    }
                    it.host.orEmpty() == Constants.DEEP_LINK_PATH_CREATE_GROUP -> {
                        viewModel.onCreateGroupClick()
                    }
                }
            }
        }
    }

    private fun setBackStackHandler() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.findFragmentById(getContainerIdFull()) == null) {
                binding.mainBottomNavigationView.selectedItemId =
                    when (supportFragmentManager.findFragmentById(getContainerId())) {
                        is HomeFragment -> R.id.home
                        is CommunityFeedFragment -> R.id.ascension_plan
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
                    this.openLink("https://course.ascend.video/")
                    //viewModel.navigateToMyContent()
                    false
                }
                R.id.ascension_plan -> {
                    viewModel.navigateToCommunityFeed()
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

    override fun setTitle(title: String) {
        binding.tvTitle.text = title.toUpperCase()
        binding.tvTitle.visible()
        binding.communityDropDown.gone()
    }

    override fun setCommunityTitle(title: String) {
        binding.tvTitle.gone()
        binding.communityDropDown.visible()
        viewModel.communities.value?.indexOfFirst {
            it.title.toUpperCase(Locale.getDefault()) == title.toUpperCase(
                Locale.getDefault()
            )
        }?.also {
            binding.communityDropDown.setSelection(it)
        }
    }

    override fun setSearchEnabled(isVisible: Boolean) {
        binding.btnSearch.visible(isVisible)
    }

    override fun setFilterEnabled(isVisible: Boolean) {
        binding.btnFilter.visible(isVisible)
    }

    override fun setChatEnabled(isVisible: Boolean) {
        binding.btnChat.visible(isVisible)
        if (isVisible.not()) {
            binding.hasChatMessages.visible(false)
        }
    }

    override fun setShareEnabled(isVisible: Boolean) {
        binding.btnShare.visible(isVisible)
    }

    override fun setShareInAppEnabled(isVisible: Boolean) {
        binding.btnShareInApp.visible(isVisible)
    }

    override fun getUnreadMessageCount() {
        viewModel.getUnreadMessageCount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            VideoChatActivity.RESULT_CODE -> {
                val result =
                    data?.extras?.get(VideoChatActivity.RESULT_TAG) as? VideoChatActivity.ResultStatus
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
        parseIntent(intent)
        intent?.extras?.get(Constants.KEY_GROUP_ID)?.let {
            viewModel.tryToNavigateToGroupInfo(it as Long)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSpinner(
        spinner: Spinner,
        listener: AdapterView.OnItemSelectedListener,
        spinnerAdapter: SpinnerAdapter,
        selectedCommunity: Int
    ) {
        spinner.adapter = spinnerAdapter
        spinner.setSelection(selectedCommunity)
        spinner.onItemSelectedListener = listener
    }

    private val communityListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.saveCommunity(
                    (binding.communityDropDown.selectedItem as PresentationCommunityModel).title.toCapitalLetter(),
                    binding.mainBottomNavigationView.selectedItemId
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    companion object {
        const val HOME_VM_TAG = "HOME_VM_TAG"
    }
}