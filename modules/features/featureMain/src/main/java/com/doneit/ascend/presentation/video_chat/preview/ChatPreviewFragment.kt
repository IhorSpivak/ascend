package com.doneit.ascend.presentation.video_chat.preview

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChatPreviewBinding
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.twilio.ITwilioChatViewDelegate
import kotlinx.android.synthetic.main.fragment_chat_preview.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChatPreviewFragment : BaseFragment<FragmentChatPreviewBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatPreviewContract.ViewModel>() with provider { vmShared<VideoChatViewModel>(instance()) }
    }

    private var delegate: ITwilioChatViewDelegate? = null

    override val viewModel: ChatPreviewContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun showVideo() {
        videoView?.visible(true)
    }
}