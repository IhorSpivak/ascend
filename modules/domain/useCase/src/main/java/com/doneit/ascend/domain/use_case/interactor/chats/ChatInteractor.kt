package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.use_case.gateway.IMyChatGateway

class ChatInteractor(
    private val chatGateway: IMyChatGateway
) : ChatUseCase {
    override fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>> {
        return chatGateway.getMyChatList(request)
    }

    override fun getMessageList(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>> {
        return chatGateway.getMessages(chatId, request)
    }

    override fun getMemberList(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>> {
        return chatGateway.getMembers(chatId, request)
    }

    override suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.createChat(createChatDTO)
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.delete(id)
    }

    override suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>> {
        return chatGateway.sendMessage(request)
    }

    override suspend fun leave(id: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.leave(id)
    }


}