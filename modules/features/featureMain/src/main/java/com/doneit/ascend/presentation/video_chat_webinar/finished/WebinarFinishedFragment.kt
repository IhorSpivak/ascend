package com.doneit.ascend.presentation.video_chat_webinar.finished

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarFinishedBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


class WebinarFinishedFragment : BaseFragment<FragmentWebinarFinishedBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarFinishedContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel: WebinarFinishedContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}