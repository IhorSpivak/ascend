package com.doneit.ascend.presentation.video_chat_webinar.in_progress

import android.Manifest
import android.content.Context
import android.hardware.Camera
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.base.BaseFragment
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
import com.haishinkit.events.Event
import com.haishinkit.events.IEventListener
import com.haishinkit.media.Audio
import com.haishinkit.rtmp.RTMPConnection
import com.haishinkit.rtmp.RTMPStream
import com.haishinkit.util.EventUtils
import kotlinx.android.synthetic.main.fragment_video_chat_webinar.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WebinarVideoChatInProgressFragment : BaseFragment<FragmentVideoChatWebinarBinding>(),
    IEventListener {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarVideoChatInProgressContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel: WebinarVideoChatInProgressContract.ViewModel by instance()

    private var delegate: VimeoChatViewDelegate? = null
    private var stream: RTMPStream? = null
    private var connection: RTMPConnection? = null

    private val audioManager by lazy {
        activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val adapter: FourQuestionsAdapter by lazy { FourQuestionsAdapter() }
    private var previousAudioMode = 0
    private var previousMicrophoneMute = false

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.send.setOnClickListener {
            if (binding.message.text.isNotBlank()) {
                viewModel.createQuestion(binding.message.text.trim().toString())
                binding.message.text.clear()
            }
        }
        binding.rvQuestions.adapter = adapter
        (binding.rvQuestions.layoutManager as LinearLayoutManager).reverseLayout = true
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
                    (binding.rvQuestions.layoutManager as LinearLayoutManager).scrollToPosition(0)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        delegate?.onStart()
        viewModel.credentials.observe(this, Observer {
            if (it.key != null && it.link != null && it.role == ChatRole.OWNER)
                startVideo(it)
        })
    }

    override fun onPause() {
        connection?.close()
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
                //TODO: remove
                if (true) return@requestPermissions
                connection = RTMPConnection()
                stream = RTMPStream(connection!!)
                stream?.attachCamera(com.haishinkit.media.Camera(Camera.open()))
                stream?.attachAudio(Audio())
                connection?.addEventListener("rtmpStatus", this)
                connection?.connect(RTMP_LINK)
                viewModel.switchCameraEvent.observe(this) {
                    delegate?.switchCamera()
                }
                binding.videoView.attachStream(stream!!)
            },
            onDenied = {
                viewModel.onPermissionsRequired(WebinarVideoChatActivity.ResultStatus.POPUP_REQUIRED)
            }
        )
    }

    private fun configureAudio(enable: Boolean) {
        with(audioManager) {
            if (enable) {
                previousAudioMode = audioManager.mode
                requestAudioFocus()

                mode = AudioManager.MODE_IN_COMMUNICATION
                /*
                 * Always disable microphone mute during a WebRTC call.
                 */
                previousMicrophoneMute = isMicrophoneMute
                isMicrophoneMute = false
            } else {
                mode = previousAudioMode
                abandonAudioFocus(null)
                isMicrophoneMute = previousMicrophoneMute
            }
        }
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { }
                .build()
            audioManager.requestAudioFocus(focusRequest)
        } else {
            audioManager.requestAudioFocus(
                null, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    companion object {
        const val DELAY_HIDE_SEND_BADGE = 2000L
        const val RTMP_LINK = "rtmp://rtmp-global.cloud.vimeo.com/live"
    }

    override fun handleEvent(event: Event) {
        val data = EventUtils.toMap(event)
        val code = data["code"].toString()
        if (code == RTMPConnection.Code.CONNECT_SUCCESS.rawValue) {
            stream?.publish(viewModel.credentials.value!!.key)
            //viewModel.getM3u8Playback()
        }
    }
}