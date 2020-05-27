package com.doneit.ascend.presentation.video_chat.delegates

import android.content.Context
import android.view.View
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.twilio.ITwilioChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewModelDelegate
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo.VimeoChatViewDelegate
import com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo.VimeoChatViewModelDelegate
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient

object VideoChatUtils {
    private const val vimeoToken = "404877188e63afd0ef38e6e6d320bd89"

    fun init(context: Context) {
        val config = Configuration.Builder(vimeoToken)
            .setCacheDirectory(context.cacheDir)
            .build()

        VimeoClient.initialize(config)
    }


    fun twillioViewModelDelegate(
        viewModel: VideoChatViewModel
    ): TwilioChatViewModelDelegate {
        return TwilioChatViewModelDelegate(viewModel)
    }

    fun vimeoViewModelDelegate(
        viewModel: WebinarVideoChatViewModel
    ): VimeoChatViewModelDelegate {
        return VimeoChatViewModelDelegate(viewModel)
    }

    fun newVimeoViewModelDelegate(
        viewModelDelegate: VimeoChatViewModelDelegate?,
        placeholder: View,
        setupVimeo: VimeoChatViewDelegate.() -> Unit
    ): VimeoChatViewDelegate? {
        return viewModelDelegate?.let {
            VimeoChatViewDelegate(
                viewModelDelegate,
                placeholder
            ).apply(setupVimeo)
        }
    }

    fun newTwilioViewModelDelegate(
        viewModelDelegate: TwilioChatViewModelDelegate?,
        placeholder: View,
        setupTwilio: TwilioChatViewDelegate.() -> Unit
    ): ITwilioChatViewDelegate? {
        return viewModelDelegate?.let {
            TwilioChatViewDelegate(
                viewModelDelegate,
                placeholder
            ).apply(setupTwilio)
        }
    }
}