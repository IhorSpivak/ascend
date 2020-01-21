package com.doneit.ascend.presentation.video_chat

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.presentation.dialog.ChatParticipantActions
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityVideoChatBinding
import com.doneit.ascend.presentation.utils.extensions.visible
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

    private val viewModel: VideoChatContract.ViewModel by instance()
    private lateinit var binding: ActivityVideoChatBinding
    private val participantsAdapter by lazy { ChatParticipantsAdapter{
        showUserActions(it)
    } }

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
    }

    override fun onNetworkStateChanged(hasConnection: Boolean) {
        connectionLostView.visible(hasConnection.not())
    }

    override fun onBackPressed() {
        viewModel.onBackClick()
    }

    private fun showUserActions(user: SocketEventEntity){
        ChatParticipantActions.create(
            this,
            user
        ).show()
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