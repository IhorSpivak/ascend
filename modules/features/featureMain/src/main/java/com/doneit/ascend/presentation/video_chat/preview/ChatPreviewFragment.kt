package com.doneit.ascend.presentation.video_chat.preview

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChatPreviewBinding
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatUtils
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


        viewModel.credentials.observe(viewLifecycleOwner, Observer {
            delegate =
                VideoChatUtils.newTwilioViewModelDelegate(viewModel.viewModelDelegate, ivPreview) {
                    fragment = this@ChatPreviewFragment
                    videoView = this@ChatPreviewFragment.videoView
                }

            viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
                delegate?.enableVideo(it)
            })

            viewModel.isAudioRecording.observe(viewLifecycleOwner, Observer {
                delegate?.enableAudio(false)
            })

            startVideo(it)
        })

    }

    private fun startVideo(model: StartVideoModel) {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {
                delegate?.startVideo(model)
                viewModel.switchCameraEvent.observe(this) {
                    delegate?.switchCamera()
                }
                delegate?.startSelfVideoDisplay()
                delegate?.enableAudio(false)
            },
            onDenied = {
                viewModel.onPermissionsRequired(VideoChatActivity.ResultStatus.POPUP_REQUIRED)
            }
        )
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

    private fun clearRenderers() {
        delegate?.clearRenderers()
    }
}