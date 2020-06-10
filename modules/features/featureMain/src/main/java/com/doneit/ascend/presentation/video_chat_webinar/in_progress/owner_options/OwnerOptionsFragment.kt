package com.doneit.ascend.presentation.video_chat_webinar.in_progress.owner_options

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarOwnerOptionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.common.OnUserInteractionListener
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class OwnerOptionsFragment : BaseFragment<FragmentWebinarOwnerOptionsBinding>(),
    OnUserInteractionListener {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<OwnerOptionsContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: OwnerOptionsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }

    private var timer = Timer()

    override fun onResume() {
        super.onResume()
        timer.cancel()
        timer.purge()
        timer = Timer()
        resetTimer()
    }

    private fun resetTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModel.onBackClick()
                timer.cancel()
                timer.purge()
            }
        }, DELAY_EXIT)
    }


    override fun onUserInteraction() {
        timer.cancel()
        timer.purge()
        timer = Timer()
        resetTimer()
    }

    override fun onPause() {
        timer.cancel()
        timer.purge()
        super.onPause()
    }


    override fun onDestroyView() {
        timer.cancel()
        timer.purge()
        super.onDestroyView()
    }

    companion object {
        private const val DELAY_EXIT = 5000L
        private const val IS_DIALOG_SHOWN_KEY = "IS_DIALOG_SHOWN_KEY"
    }

}