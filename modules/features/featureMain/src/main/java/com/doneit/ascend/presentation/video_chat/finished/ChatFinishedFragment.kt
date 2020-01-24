package com.doneit.ascend.presentation.video_chat.finished

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChatFinishedBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChatFinishedFragment : BaseFragment<FragmentChatFinishedBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatFinishedContract.ViewModel>() with provider {
            vmShared<VideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel: ChatFinishedContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}