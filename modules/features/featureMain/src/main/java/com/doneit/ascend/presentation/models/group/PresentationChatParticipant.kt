package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantMultilistener
import com.twilio.video.LocalParticipant
import com.twilio.video.RemoteParticipant

data class PresentationChatParticipant(
    val source: ParticipantSourcePriority,
    val userId: String = DEFAULT_MODEL_ID.toString(),
    val fullName: String? = null,
    val image: ImageEntity? = null,
    var isHandRisen: Boolean = false,
    var isSpeaker: Boolean = false,
    var isMuted: Boolean = false,
    val remoteParticipant: RemoteParticipant? = null,
    val localParticipant: LocalParticipant? = null,
    private val multilistenr: RemoteParticipantMultilistener = RemoteParticipantMultilistener(),
    var isOwner: Boolean = false
) : Comparable<PresentationChatParticipant> {

    private var primaryVideoListener: RemoteParticipantListener? = null
    private var secondaryVideoListener: RemoteParticipantListener? = null

    init {
        remoteParticipant?.setListener(multilistenr)
    }

    fun setPrimaryVideoListener(listener: RemoteParticipantListener) {
        removePrimaryVideoListener()
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
        removeSecondaryVideoListener()
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
        var isEqual = true

        if (source.ordinal != other.source.ordinal) {
            isEqual = false
        }

        if (userId != other.userId) {
            isEqual = false
        }

        if (fullName != other.fullName) {
            isEqual = false
        }

        if (image?.url != other.image?.url) {
            isEqual = false
        }

        if (isHandRisen != other.isHandRisen) {
            isEqual = false
        }

        if (isSpeaker != other.isSpeaker) {
            isEqual = false
        }

        if (isMuted != other.isMuted) {
            isEqual = false
        }

        if (remoteParticipant != other.remoteParticipant) {
            isEqual = false
        }

        if (localParticipant != other.localParticipant) {
            isEqual = false
        }

        if (isOwner != other.isOwner) {
            isEqual = false
        }

        return if (isEqual) {
            0
        } else {
            source.ordinal - other.source.ordinal
        }
    }

}