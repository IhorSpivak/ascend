package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface ChatUseCase {
    fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    fun getMessageList(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>
    fun getMemberList(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>
    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>

}