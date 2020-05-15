package com.doneit.ascend.presentation.video_chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateChatParticipantBinding
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantListener
import com.twilio.video.RemoteAudioTrackPublication
import com.twilio.video.RemoteParticipant
import com.twilio.video.RemoteVideoTrackPublication
import com.twilio.video.VideoTrack
import java.lang.ref.WeakReference

class ChatParticipantViewHolder(
    private val binding: TemplateChatParticipantBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var lastModel: WeakReference<PresentationChatParticipant>? = null

    fun bind(model: PresentationChatParticipant) {
        binding.name = model.fullName
        binding.isHandRisen = model.isHandRisen
        binding.isSpeaker = model.isSpeaker
        binding.isMuted = model.isMuted
        lastModel?.get()?.getVideoTrack()?.removeRenderer(binding.videoView)
        model.getVideoTrack()?.let {
            lastModel = WeakReference(model)
            it.addRenderer(binding.videoView)
            binding.videoView.visible(true)
        }
        model.setSecondaryVideoListener(getParticipantsListener(model))
        if(binding.url != model.image?.thumbnail?.url) {
            binding.url = model.image?.thumbnail?.url
        }
    }

    fun clear() {
        lastModel?.get()?.removeSecondaryVideoListener()
    }

    private fun PresentationChatParticipant.getVideoTrack(): VideoTrack? {
        return remoteParticipant?.videoTracks?.firstOrNull()?.videoTrack
    }

    private fun getParticipantsListener(model: PresentationChatParticipant): RemoteParticipantListener {
        return object : RemoteParticipantListener() {
            override fun onAudioTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                //binding.ivMicroOff.visible(false)
                model.isMuted = false
            }

            override fun onAudioTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                model.isMuted = true
                //binding.ivMicroOff.visible(true)
            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                binding.videoView.visible(true)
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                binding.videoView.visible(false)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ChatParticipantViewHolder {
            val binding: TemplateChatParticipantBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_chat_participant,
                parent,
                false
            )

            return ChatParticipantViewHolder(binding)
        }
    }
}