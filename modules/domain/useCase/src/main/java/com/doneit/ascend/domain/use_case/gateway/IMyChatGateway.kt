package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface IMyChatGateway {
    fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>>

    fun getMessages(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>

    fun getMembers(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>

    suspend fun getMemberList(chatId: Long, request: MemberListDTO): ResponseEntity<List<MemberEntity>, List<String>>

    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>>

    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>

    suspend fun updateChat(
        id: Long,
        title: String? = null,
        chatMembers: List<Int>? = null
    ): ResponseEntity<ChatEntity, List<String>>

    val messagesStream: LiveData<MessageSocketEntity>

    fun connectToChannel(id: Long)

    fun disconnect()
}