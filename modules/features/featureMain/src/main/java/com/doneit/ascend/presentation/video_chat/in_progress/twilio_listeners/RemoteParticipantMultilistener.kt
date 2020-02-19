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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioTrackPublished(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoTrackPublished(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        remoteVideoTrack: RemoteVideoTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        remoteVideoTrack: RemoteVideoTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        twilioException: TwilioException
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        remoteDataTrack: RemoteDataTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        remoteAudioTrack: RemoteAudioTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioTrackSubscribed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        remoteAudioTrack: RemoteAudioTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication,
        twilioException: TwilioException
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioTrackSubscriptionFailed(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication,
        twilioException: TwilioException
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteAudioTrackPublication: RemoteAudioTrackPublication
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataTrackUnsubscribed(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication,
        remoteDataTrack: RemoteDataTrack
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataTrackUnpublished(
        remoteParticipant: RemoteParticipant,
        remoteDataTrackPublication: RemoteDataTrackPublication
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}