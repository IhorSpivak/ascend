package com.doneit.ascend.presentation.video_chat.preview

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChatPreviewBinding
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.in_progress.HeadphonesBroadcastReceiver
import com.twilio.video.CameraCapturer
import com.twilio.video.LocalVideoTrack
import kotlinx.android.synthetic.main.fragment_chat_preview.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChatPreviewFragment : BaseFragment<FragmentChatPreviewBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatPreviewContract.ViewModel>() with provider { vmShared<VideoChatViewModel>(instance()) }
    }

    private val audioManager by lazy {
        activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var isSpeakerPhoneEnabled = true
        set(value) {
            field = value
            audioManager.isSpeakerphoneOn = field
        }
    private val brHeadphones = HeadphonesBroadcastReceiver {
        isSpeakerPhoneEnabled = it
    }

    override val viewModel: ChatPreviewContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }

    override fun onStart() {
        super.onStart()
        startVideo()
    }

    override fun onStop() {
        viewModel.localVideoTrack?.removeRenderer(videoView)
        super.onStop()
    }

    private fun startVideo() {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {

                if (viewModel.localVideoTrack == null) {
                    val capturer = CameraCapturer(
                        activity!!.applicationContext,
                        VideoChatViewModel.cameraSources.first()
                    )

                    viewModel.localVideoTrack =
                        LocalVideoTrack.create(activity!!.applicationContext, true, capturer)!!
                }
                viewModel.localVideoTrack?.let {
                    it.addRenderer(videoView)
                    showVideo()
                }

                viewModel.switchCameraEvent.observe(this) {
                    (viewModel.localVideoTrack?.videoCapturer as? CameraCapturer)?.switchCamera()
                }
            },
            onDenied = {
                viewModel.onPermissionsRequired(VideoChatActivity.ResultStatus.POPUP_REQUIRED)
            }
        )
    }

    private fun showVideo() {
        videoView?.visible(true)
    }
}