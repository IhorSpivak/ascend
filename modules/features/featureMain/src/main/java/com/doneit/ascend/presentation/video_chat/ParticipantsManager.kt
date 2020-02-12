package com.doneit.ascend.presentation.video_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.models.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.Constants.LIST_INDEX_ABSENT

class ParticipantsManager {
    private val _participants = MutableLiveData<List<PresentationChatParticipant>>(listOf())
    val participants: LiveData<List<PresentationChatParticipant>> = _participants

    private fun getParticipantMList(): MutableList<PresentationChatParticipant> {
        return _participants.value?.toMutableList() ?: mutableListOf()
    }

    fun addParticipant(newParticipant: PresentationChatParticipant, isMergePriorityLocale: Boolean = true) {
        val resultList = getParticipantMList()

        val index = resultList.indexOfFirst { it.userId == newParticipant.userId }
        if (index == LIST_INDEX_ABSENT) {
            resultList.add(newParticipant)
        } else {
            resultList[index] = resultList[index].merge(newParticipant, isMergePriorityLocale)
        }

        _participants.postValue(resultList)
    }

    fun removeParticipant(participant: PresentationChatParticipant) {
        val resultList = getParticipantMList()
        resultList.removeAll { it.userId == participant.userId }
        _participants.postValue(resultList)
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
        val resultList = getParticipantMList()
        resultList.forEach {
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
            }

            res
        }

        _participants.postValue(resultList)
    }

    fun isSpeaker(id: String): Boolean {
        return getParticipantMList().firstOrNull { it.userId == id }?.isSpeaker == true
    }

    fun addParticipants(newList: List<PresentationChatParticipant>?, isMergePriorityLocale: Boolean = true) {
        val resultList = getParticipantMList()

        newList?.forEach { remoteItem ->
            val index = resultList.indexOfFirst { it.userId == remoteItem.userId }
            if (index == LIST_INDEX_ABSENT) {
                resultList.add(remoteItem)
            } else {
                resultList[index] = resultList[index].merge(remoteItem, isMergePriorityLocale)
            }
        }

        _participants.postValue(resultList)
    }

    private fun updateParticipant(participant: PresentationChatParticipant) {
        val resultList = getParticipantMList()

        val index = resultList.indexOfFirst { it.userId == participant.userId }
        if (index != LIST_INDEX_ABSENT) {
            resultList.removeAt(index)
            resultList.add(index, participant)
            _participants.postValue(resultList)
        }
    }

    private fun PresentationChatParticipant.merge(newParticipant: PresentationChatParticipant, isMergePriorityLocale: Boolean): PresentationChatParticipant {
        return PresentationChatParticipant(
            userId,
            fullName ?: newParticipant.fullName,
            newParticipant.image ?: image,
            if(isMergePriorityLocale) isHandRisen else newParticipant.isHandRisen,
            if(isMergePriorityLocale) isSpeaker else newParticipant.isSpeaker,
            if(isMergePriorityLocale) isMuted else newParticipant.isMuted,
            if(isMergePriorityLocale) remoteParticipant ?: newParticipant.remoteParticipant else newParticipant.remoteParticipant ?: remoteParticipant
        )
    }
}