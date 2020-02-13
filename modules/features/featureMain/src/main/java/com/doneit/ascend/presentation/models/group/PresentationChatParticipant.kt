package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantMultilistener
import com.twilio.video.RemoteParticipant

data class PresentationChatParticipant(
    val source: ParticipantSourcePriority,
    val userId: String = DEFAULT_MODEL_ID.toString(),
    val fullName: String? = null,
    val image: ImageEntity? = null,
    val isHandRisen: Boolean = false,
    val isSpeaker: Boolean = false,
    val isMuted: Boolean = false,
    val remoteParticipant: RemoteParticipant? = null,
    private val multilistenr: RemoteParticipantMultilistener = RemoteParticipantMultilistener()
) : Comparable<PresentationChatParticipant> {

    private var primaryVideoListener: RemoteParticipantListener? = null
    private var secondaryVideoListener: RemoteParticipantListener? = null

    init {
        remoteParticipant?.setListener(multilistenr)
    }

    fun setPrimaryVideoListener(listener: RemoteParticipantListener) {
        multilistenr.addListener(listener)
        primaryVideoListener = listener
    }

    fun removePrimaryVideoListener() {
        primaryVideoListener?.let {
            multilistenr.removeListener(it)
            primaryVideoListener = null
        }
    }

    fun setSecondaryVideoListener(listener: RemoteParticipantListener) {
        multilistenr.addListener(listener)
        secondaryVideoListener = listener
    }

    fun removeSecondaryVideoListener() {
        secondaryVideoListener?.let {
            multilistenr.removeListener(it)
            secondaryVideoListener = null
        }
    }

    override fun compareTo(other: PresentationChatParticipant): Int {
        return source.ordinal - other.source.ordinal
    }
}