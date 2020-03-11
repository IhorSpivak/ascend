package com.doneit.ascend.presentation.video_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.Constants.LIST_INDEX_ABSENT

class ParticipantsManager {
    private val _participants = MutableLiveData<List<PresentationChatParticipant>>(listOf())
    private val _currentSpeaker = MutableLiveData<PresentationChatParticipant?>(null)

    val participants: LiveData<List<PresentationChatParticipant>> = _participants
    val currentSpeaker: LiveData<PresentationChatParticipant?> = _currentSpeaker

    private fun getParticipantMList(): MutableList<PresentationChatParticipant> {
        return _participants.value?.toMutableList() ?: mutableListOf()
    }

    fun addParticipant(newParticipant: PresentationChatParticipant) {
        val resultList = getParticipantMList()

        val index = resultList.indexOfFirst { it.userId == newParticipant.userId }
        if (index == LIST_INDEX_ABSENT) {
            resultList.add(newParticipant)
        } else {
            resultList[index] = resultList[index].merge(newParticipant)
        }
        /*if (newParticipant.isSpeaker){
            _currentSpeaker.postValue(newParticipant)
        }*/
        _participants.value = resultList
    }

    fun removeParticipant(participant: PresentationChatParticipant) {
        val resultList = getParticipantMList()
        resultList.removeAll { it.userId == participant.userId }
        _participants.postValue(resultList)
    }

    fun addParticipants(newList: List<PresentationChatParticipant>?) {
        val resultList = getParticipantMList()

        newList?.forEach { remoteItem ->
            val index = resultList.indexOfFirst { it.userId == remoteItem.userId }
            if (index == LIST_INDEX_ABSENT) {
                resultList.add(remoteItem)
            } else {
                resultList[index] = resultList[index].merge(remoteItem)
            }
        }
        /*newList?.forEach {
            if (it.isSpeaker){
                _currentSpeaker.postValue(it)
            }
        }*/
        _participants.value = resultList
    }

    private fun updateParticipant(participant: PresentationChatParticipant) {
        val resultList = getParticipantMList()

        val index = resultList.indexOfFirst { it.userId == participant.userId }
        if (index != LIST_INDEX_ABSENT) {
            resultList.removeAt(index)
            resultList.add(index, participant)
            _participants.value = resultList
        }
    }

    private fun PresentationChatParticipant.merge(newParticipant: PresentationChatParticipant): PresentationChatParticipant {
        val first = maxOf(this, newParticipant)
        val second = minOf(this, newParticipant)
        val resultSource = maxOf(this.source, newParticipant.source)

        return this.copy(
            source = resultSource,
            userId = userId,
            fullName = first.fullName ?: second.fullName,
            image = first.image ?: second.image,
            isHandRisen = first.isHandRisen,
            isSpeaker = first.isSpeaker,
            isMuted = first.isMuted,
            remoteParticipant = first.remoteParticipant ?: second.remoteParticipant
        )
    }

    fun updateHandState(participant: PresentationChatParticipant) {
        getParticipantMList().firstOrNull { it.userId == participant.userId }?.let {
            updateParticipant(it.copy(
                isHandRisen = participant.isHandRisen
            ))
        }
    }

    fun mute(participant: PresentationChatParticipant) {
        getParticipantMList().firstOrNull { it.userId == participant.userId }?.let {
            updateParticipant(it.copy(
                isMuted = true
            ))
        }
    }

    fun unmute(participant: PresentationChatParticipant) {
        getParticipantMList().firstOrNull { it.userId == participant.userId }?.let {
            updateParticipant(it.copy(
                isMuted = false
            ))
        }
    }

    fun onSpeakerChanged(id: String?) {
        val resultList = getParticipantMList().map {
            var res = it

            //new model only if field has changed
            if(it.userId == id) {
                res = it.copy(
                    isSpeaker = true
                )
            } else if(it.isSpeaker) {
                res = it.copy(
                    isSpeaker = false
                )
                it.removePrimaryVideoListener()
            }

            res
        }

        val speaker = resultList.firstOrNull { it.userId == id }
        _currentSpeaker.postValue(speaker)

        _participants.value = resultList
    }

    fun isSpeaker(id: String): Boolean {
        return _currentSpeaker.value?.userId == id
    }
}