package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.CreateChatDTO

interface IMyChatGateway {
    fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
    suspend fun updateChat(
        id: Long,
        createChatDTO: CreateChatDTO
    ): ResponseEntity<ChatEntity, List<String>>
}