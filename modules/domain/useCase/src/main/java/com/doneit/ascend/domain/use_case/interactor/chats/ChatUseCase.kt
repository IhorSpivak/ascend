package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO

interface ChatUseCase {
    fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>

}