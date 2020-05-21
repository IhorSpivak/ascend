package com.doneit.ascend.presentation.video_chat_webinar.preview

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarChatPreviewBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WebinarChatPreviewFragment : BaseFragment<FragmentWebinarChatPreviewBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarChatPreviewContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: ChatPreviewContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}