package com.doneit.ascend.presentation.video_chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateChatParticipantBinding
import com.doneit.ascend.presentation.models.PresentationChatParticipant
import com.twilio.video.VideoTrack
import java.lang.ref.WeakReference

class ChatParticipantViewHolder(
    private val binding: TemplateChatParticipantBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var lastRenderer: WeakReference<VideoTrack>? = null

    fun bind(model: PresentationChatParticipant) {
        binding.name = model.fullName
        binding.isHandRisen = model.isHandRisen
        binding.isSpeaker = model.isSpeaker

        lastRenderer?.get()?.removeRenderer(binding.videoView)
        model.remoteParticipant?.videoTracks?.firstOrNull()?.videoTrack?.let {
            lastRenderer = WeakReference(it)
            it.addRenderer(binding.videoView)
        }

        if(binding.url != model.image?.thumbnail?.url) {
            binding.url = model.image?.thumbnail?.url
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