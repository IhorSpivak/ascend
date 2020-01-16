package com.doneit.ascend.presentation.video_chat

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityVideoChatBinding
import com.doneit.ascend.presentation.video_chat.common.ChatParticipantsAdapter
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

    private val viewModel: VideoChatContract.ViewModel by instance()
    private lateinit var binding: ActivityVideoChatBinding
    private val participantsAdapter by lazy { ChatParticipantsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_chat)
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.rvParticipants.adapter = participantsAdapter

        val groupId = intent.getLongExtra(GROUP_ID_ARG, -1)
        viewModel.init(groupId)
    }

    override fun onBackPressed() {
        viewModel.onBackClick()
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