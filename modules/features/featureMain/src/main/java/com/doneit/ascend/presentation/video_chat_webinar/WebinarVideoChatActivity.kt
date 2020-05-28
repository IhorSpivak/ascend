package com.doneit.ascend.presentation.video_chat_webinar

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityWebinarVideoChatBinding
import com.doneit.ascend.presentation.video_chat.common.ChatParticipantsAdapter
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.common.OnUserInteractionListener
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WebinarVideoChatActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("WebinarVideoChatActivity") {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<WebinarVideoChatRouter>() with provider {
            WebinarVideoChatRouter(
                this@WebinarVideoChatActivity
            )
        }

        bind<WebinarVideoChatContract.Router>() with provider { instance<WebinarVideoChatRouter>() }

        bind<ViewModel>(WebinarVideoChatViewModel::class.java.simpleName) with provider {
            WebinarVideoChatViewModel(
                instance(),
                instance(),
                instance()
            )
        }

        bind<WebinarVideoChatContract.ViewModel>() with provider {
            vm<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    fun getContainerId() = R.id.container
    fun getFullContainerId() = R.id.root_container

    private val router: WebinarVideoChatContract.Router by instance()
    private val viewModel: WebinarVideoChatContract.ViewModel by instance()
    private lateinit var binding: ActivityWebinarVideoChatBinding
    private val participantsAdapter by lazy {
        ChatParticipantsAdapter {
            viewModel.onParticipantClick(it)
        }
    }

    var userInteractionListener: OnUserInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar_video_chat)
        binding.lifecycleOwner = this
        binding.model = viewModel

        val groupId = intent.getLongExtra(GROUP_ID_ARG, -1)
        viewModel.init(groupId)

        viewModel.navigation.observe(this, Observer {
            handleNavigation(it)
        })
    }

    private fun handleNavigation(action: WebinarVideoChatContract.Navigation) {
        when (action) {
            WebinarVideoChatContract.Navigation.BACK -> if (router.canGoBack()) router.onBack() else viewModel.finishCall()
            WebinarVideoChatContract.Navigation.FINISH_ACTIVITY -> router.finishActivity()
            WebinarVideoChatContract.Navigation.TO_PREVIEW -> router.navigateToPreview()
            WebinarVideoChatContract.Navigation.TO_CHAT_IN_PROGRESS -> router.navigateToChatInProgress()
            WebinarVideoChatContract.Navigation.TO_CHAT_FINISH -> router.navigateToChatFinishScreen()
            WebinarVideoChatContract.Navigation.TO_USER_CHAT_OPTIONS -> router.navigateUserChatOptions()
            WebinarVideoChatContract.Navigation.TO_MM_CHAT_OPTIONS -> router.navigateToMMChatOptions()
            WebinarVideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS -> {
                val userId = action.data.getString(WebinarVideoChatViewModel.USER_ID_KEY)!!
                router.navigateToChatParticipantActions(userId)
            }
            WebinarVideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG -> {
                val ordinal = action.data.getInt(WebinarVideoChatViewModel.ACTIVITY_RESULT_KEY)
                val resultCode = ResultStatus.values()[ordinal]
                router.navigateToPermissionsRequiredDialog(resultCode)
            }
            WebinarVideoChatContract.Navigation.TO_CHAT -> {
                val groupId = action.data.getLong(WebinarVideoChatViewModel.GROUP_ID_KEY)
                router.navigateToChat(groupId)
            }
            WebinarVideoChatContract.Navigation.TO_NOTES -> {
                val groupId = action.data.getLong(WebinarVideoChatViewModel.GROUP_ID_KEY)
                router.navigateToNotes(groupId)
            }
            WebinarVideoChatContract.Navigation.TO_QUESTIONS -> {
                val groupId = action.data.getLong(WebinarVideoChatViewModel.GROUP_ID_KEY)
                router.navigateToQuestions(groupId)
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userInteractionListener?.onUserInteraction()
    }

    enum class ResultStatus {
        OK,
        POPUP_REQUIRED
    }

    companion object {
        const val GROUP_ID_ARG = "GROUP_ID"

        const val RESULT_CODE = 43
        const val RESULT_TAG = "WEBINAR_VIDEO_CHAT_RESULT"
    }

}