package com.doneit.ascend.presentation.video_chat.in_progress.mm_options

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMmChatOptionsBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MMChatOptionsFragment : BaseFragment<FragmentMmChatOptionsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<MMChatOptionsContract.ViewModel>() with provider {
            vmShared<VideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: MMChatOptionsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}