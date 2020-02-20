package com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners

import com.twilio.video.*

open class RemoteParticipantMultilistener : RemoteParticipantListener() {

    private val listeners = mutableListOf<RemoteParticipantListener>()

    fun addListener(listener: RemoteParticipantListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: RemoteParticipantListener) {
        listeners.remove(listener)
    }

    override fun onAudioTrackEnabled(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        listeners.forEach {
            it.onAudioTrackEnabled(remoteParticipant, remoteAudioTrackPublication)
        }
    }

    override fun onAudioTrackDisabled(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        listeners.forEach {
            it.onAudioTrackDisabled(remoteParticipant, remoteAudioTrackPublication)
        }
    }

    override fun onVideoTrackEnabled(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackEnabled(remoteParticipant, remoteVideoTrackPublication)
        }
    }

    override fun onVideoTrackDisabled(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackDisabled(remoteParticipant, remoteVideoTrackPublication)
        }
    }

    override fun onDataTrackPublished(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication
    ) {
        listeners.forEach {
            it.onDataTrackPublished(remoteParticipant, remoteDataTrackPublication)
        }
    }

    override fun onAudioTrackPublished(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        listeners.forEach {
            it.onAudioTrackPublished(remoteParticipant, remoteAudioTrackPublication)
        }
    }

    override fun onVideoTrackPublished(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackPublished(remoteParticipant, remoteVideoTrackPublication)
        }
    }

    override fun onVideoTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        remoteVideoTrack: RemoteVideoTrack
    ) {
        listeners.forEach {
            it.onVideoTrackSubscribed(
                remoteParticipant,
                remoteVideoTrackPublication,
                remoteVideoTrack
            )
        }
    }

    override fun onVideoTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        remoteVideoTrack: RemoteVideoTrack
    ) {
        listeners.forEach {
            it.onVideoTrackUnsubscribed(
                remoteParticipant,
                remoteVideoTrackPublication,
                remoteVideoTrack
            )
        }
    }

    override fun onDataTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        twilioException: TwilioException
    ) {
        listeners.forEach {
            it.onDataTrackSubscriptionFailed(
                remoteParticipant,
                remoteDataTrackPublication,
                twilioException
            )
        }
    }

    override fun onDataTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        remoteDataTrack: RemoteDataTrack
    ) {
        listeners.forEach {
            it.onDataTrackSubscribed(remoteParticipant, remoteDataTrackPublication, remoteDataTrack)
        }
    }

    override fun onAudioTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        remoteAudioTrack: RemoteAudioTrack
    ) {
        listeners.forEach {
            it.onAudioTrackUnsubscribed(
                remoteParticipant,
                remoteAudioTrackPublication,
                remoteAudioTrack
            )
        }
    }

    override fun onAudioTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        remoteAudioTrack: RemoteAudioTrack
    ) {
        listeners.forEach {
            it.onAudioTrackSubscribed(
                remoteParticipant,
                remoteAudioTrackPublication,
                remoteAudioTrack
            )
        }
    }

    override fun onVideoTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        twilioException: TwilioException
    ) {
        listeners.forEach {
            it.onVideoTrackSubscriptionFailed(
                remoteParticipant,
                remoteVideoTrackPublication,
                twilioException
            )
        }
    }

    override fun onAudioTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        twilioException: TwilioException
    ) {
        listeners.forEach {
            it.onAudioTrackSubscriptionFailed(
                remoteParticipant,
                remoteAudioTrackPublication,
                twilioException
            )
        }
    }

    override fun onAudioTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        listeners.forEach {
            it.onAudioTrackUnpublished(remoteParticipant, remoteAudioTrackPublication)
        }
    }

    override fun onVideoTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackUnpublished(remoteParticipant, remoteVideoTrackPublication)
        }
    }

    override fun onDataTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        remoteDataTrack: RemoteDataTrack
    ) {
        listeners.forEach {
            it.onDataTrackUnsubscribed(
                remoteParticipant,
                remoteDataTrackPublication,
                remoteDataTrack
            )
        }
    }

    override fun onDataTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication
    ) {
        listeners.forEach {
            it.onDataTrackUnpublished(remoteParticipant, remoteDataTrackPublication)
        }
    }
}