package com.doneit.ascend.presentation.video_chat

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityVideoChatBinding
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.video_chat.common.ChatParticipantListAdapter
import com.doneit.ascend.presentation.video_chat.common.ChatParticipantsAdapter
import kotlinx.android.synthetic.main.activity_video_chat.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class VideoChatActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("CropActivity") {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<VideoChatRouter>() with provider {
            VideoChatRouter(
                this@VideoChatActivity
            )
        }

        bind<VideoChatContract.Router>() with provider { instance<VideoChatRouter>() }

        bind<ParticipantsManager>() with provider {
            ParticipantsManager()
        }

        bind<ViewModel>(VideoChatViewModel::class.java.simpleName) with provider {
            VideoChatViewModel(
                instance(),
                instance(),
                instance()
            )
        }

        bind<VideoChatContract.ViewModel>() with provider { vm<VideoChatViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container
    fun getFullContainerId() = R.id.root_container

    private val router: VideoChatContract.Router by instance()
    private val viewModel: VideoChatContract.ViewModel by instance()
    private lateinit var binding: ActivityVideoChatBinding
    private val participantsAdapter by lazy {
        ChatParticipantsAdapter {
            viewModel.onParticipantClick(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_chat)
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.rvParticipants.adapter = participantsAdapter

        val groupId = intent.getLongExtra(GROUP_ID_ARG, -1)
        viewModel.init(groupId)

        viewModel.participants.observe(this, Observer {
            participantsAdapter.submitList(it)
        })

        viewModel.navigation.observe(this, Observer {
            handleNavigation(it)
        })
    }


    override fun onNetworkStateChanged(hasConnection: Boolean) {
        viewModel.onNetworkStateChanged(hasConnection)
        connectionLostView.visible(hasConnection.not())
    }

    override fun onBackPressed() {
        viewModel.onBackClick()
    }

    private fun handleNavigation(action: VideoChatContract.Navigation) {
        when (action) {
            VideoChatContract.Navigation.BACK -> if (router.canGoBack()) router.onBack() else viewModel.finishCall()
            VideoChatContract.Navigation.FINISH_ACTIVITY -> router.finishActivity()
            VideoChatContract.Navigation.TO_PREVIEW -> router.navigateToPreview()
            VideoChatContract.Navigation.TO_CHAT_IN_PROGRESS -> router.navigateToChatInProgress()
            VideoChatContract.Navigation.TO_CHAT_FINISH -> router.navigateToChatFinishScreen()
            VideoChatContract.Navigation.TO_USER_CHAT_OPTIONS -> router.navigateUserChatOptions()
            VideoChatContract.Navigation.TO_MM_CHAT_OPTIONS -> router.navigateToMMChatOptions()
            VideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS -> {
                val userId = action.data.getString(VideoChatViewModel.USER_ID_KEY)!!
                router.navigateToChatParticipantActions(userId)
            }
            VideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG -> {
                val ordinal = action.data.getInt(VideoChatViewModel.ACTIVITY_RESULT_KEY)
                val resultCode = ResultStatus.values()[ordinal]
                router.navigateToPermissionsRequiredDialog(resultCode)
            }
            VideoChatContract.Navigation.TO_ATTACHMENTS -> {
                val groupId = action.data.getLong(VideoChatViewModel.GROUP_ID_KEY)
                router.navigateToAttachments(groupId)
            }
            VideoChatContract.Navigation.TO_NOTES -> {
                val groupId = action.data.getLong(VideoChatViewModel.GROUP_ID_KEY)
                router.navigateToNotes(groupId)
            }
            VideoChatContract.Navigation.TO_GOAL -> {
                val groupId = action.data.getLong(VideoChatViewModel.GROUP_ID_KEY)
                router.navigateToGoal(groupId)
            }
        }
    }

    enum class ResultStatus {
        OK,
        POPUP_REQUIRED
    }

    companion object {
        const val GROUP_ID_ARG = "GROUP_ID"

        const val RESULT_CODE = 42
        const val RESULT_TAG = "VIDEO_CHAT_RESULT"
    }
}