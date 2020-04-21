package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.domain.entity.dto.MemberListDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO

interface IMyChatGateway {
    fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    fun getMessages(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>
    fun getMembers(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
    suspend fun updateChat(id: Long, createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
}