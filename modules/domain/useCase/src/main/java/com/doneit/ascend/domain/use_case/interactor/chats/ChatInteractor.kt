package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.use_case.gateway.IMyChatGateway

class ChatInteractor(
    private val chatGateway: IMyChatGateway
) : ChatUseCase {
    override fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>> {
        return chatGateway.getMyChatList(request)
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.delete(id)
    }

    override suspend fun leave(id: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.leave(id)
    }


}