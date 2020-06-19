package com.doneit.ascend.presentation.video_chat_webinar.in_progress

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatWebinarBinding
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.utils.extensions.hide
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatUtils
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatActivity
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo.VimeoChatViewDelegate
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.common.FourQuestionsAdapter
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import kotlinx.android.synthetic.main.fragment_video_chat_webinar.*
import net.ossrs.rtmp.ConnectCheckerRtmp
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WebinarVideoChatInProgressFragment : BaseFragment<FragmentVideoChatWebinarBinding>(),
    ConnectCheckerRtmp, SurfaceHolder.Callback {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarVideoChatInProgressContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel: WebinarVideoChatInProgressContract.ViewModel by instance()

    private var delegate: VimeoChatViewDelegate? = null
    private var rtmpCamera: RtmpCamera2? = null

    private val adapter: FourQuestionsAdapter by lazy { FourQuestionsAdapter() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.send.setOnClickListener {
            if (binding.message.text.isNotBlank()) {
                viewModel.createQuestion(binding.message.text.trim().toString())
                binding.message.text.clear()
            }
        }
        binding.rvQuestions.adapter = adapter
        delegate =
            VideoChatUtils.newVimeoViewModelDelegate(viewModel.viewModelDelegate, placeholder) {
                fragment = this@WebinarVideoChatInProgressFragment
            }
        observeEvents()
        addAdapterDataObserver()
    }

    private fun observeEvents() {
        viewModel.questions.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.showMessgeSent.observe(this, Observer {
            binding.questionSent.show()
            binding.questionSent.postDelayed({
                if (isVisible) {
                    binding.questionSent.hide()
                }
            }, DELAY_HIDE_SEND_BADGE)
        })
    }

    private fun addAdapterDataObserver() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 0) {
                    (binding.rvQuestions.layoutManager as LinearLayoutManager).scrollToPosition(
                        0
                    )
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        delegate?.onStart()
        viewModel.m3u8url.observe(viewLifecycleOwner, Observer {
            binding.videoViewForParticipants.apply {
                setVideoURI(Uri.parse(it))
                visible()
                start()
            }
        })
        surfaceView.holder.addCallback(this)
    }

    override fun onPause() {
        rtmpCamera?.stopPreview()
        rtmpCamera?.stopStream()
        super.onPause()
    }

    private fun startVideo(model: StartWebinarVideoModel) {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {
                delegate?.startVideo(model)
                rtmpCamera = RtmpCamera2(surfaceView, this).apply {
                    startPreview(CameraHelper.Facing.FRONT)
                    if (!isStreaming) {
                        if (prepareAudio() && prepareVideo()) {
                            startStream(RTMP_LINK + "/" + model.key)
                        }

                    }
                }
                viewModel.switchCameraEvent.observe(this) {
                    delegate?.switchCamera()
                    rtmpCamera?.switchCamera()
                }
                viewModel.isAudioRecording.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        rtmpCamera?.enableAudio()
                    } else {
                        rtmpCamera?.disableAudio()
                    }
                })
                viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
                    if(it) {
                        rtmpCamera?.glInterface?.unMuteVideo()
                    } else {
                        rtmpCamera?.glInterface?.muteVideo()
                    }
                })
            },
            onDenied = {
                viewModel.onPermissionsRequired(WebinarVideoChatActivity.ResultStatus.POPUP_REQUIRED)
            }
        )
    }

    companion object {
        const val DELAY_HIDE_SEND_BADGE = 2000L
        const val RTMP_LINK = "rtmp://rtmp-global.cloud.vimeo.com/live"
    }

    override fun onAuthSuccessRtmp() {
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

    override fun onConnectionSuccessRtmp() {
    }

    override fun onConnectionFailedRtmp(reason: String) {
    }

    override fun onAuthErrorRtmp() {
    }

    override fun onDisconnectRtmp() {
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        rtmpCamera?.startPreview()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        viewModel.credentials.observe(this, Observer {
            if (it.key != null && it.link != null && it.role == ChatRole.OWNER)
                startVideo(it)
        })
    }
}