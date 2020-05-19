package com.doneit.ascend.presentation.video_chat.delegates

import android.content.Context
import android.view.View
import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewModelDelegate
import com.doneit.ascend.presentation.video_chat.delegates.vimeo.VimeoChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.vimeo.VimeoChatViewModelDelegate
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient

object VideoChatUtils {
    private const val vimeoToken = ""

    fun init(context: Context) {
        val config = Configuration.Builder(vimeoToken)
            .setCacheDirectory(context.cacheDir)
            .build()

        VimeoClient.initialize(config)
    }


    fun newViewModelDelegate(
        viewModel: VideoChatViewModel,
        creds: GroupCredentialsDTO
    ): VideoChatViewModelDelegate {
        return when (creds) {
            is GroupCredentialsDTO.TwilioCredentialsDTO -> TwilioChatViewModelDelegate(viewModel)
            is GroupCredentialsDTO.VimeoCredentialsDTO -> VimeoChatViewModelDelegate(viewModel)
        }
    }

    fun newViewDelegate(
        viewModelDelegate: VideoChatViewModelDelegate?,
        placeholder: View,
        setupTwilio: TwilioChatViewDelegate.() -> Unit,
        setupVimeo: VimeoChatViewDelegate.() -> Unit
    ): VideoChatViewDelegate? {
        return when (viewModelDelegate) {
            is TwilioChatViewModelDelegate -> TwilioChatViewDelegate(
                viewModelDelegate,
                placeholder
            ).apply(setupTwilio)
            is VimeoChatViewModelDelegate -> VimeoChatViewDelegate(
                viewModelDelegate,
                placeholder
            ).apply(setupVimeo)
            else -> null
        }
    }
}