package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import kotlinx.coroutines.CoroutineScope

interface ChatUseCase {
    fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    fun getChatList(request: ChatListDTO): PagedList<ChatEntity>
    fun getMessageList(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>
    suspend fun getChatDetails(id: Long): ResponseEntity<ChatEntity, List<String>>

    fun getMemberList(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>
    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun removeUser(
        chatId: Long,
        userId: Long
    ): ResponseEntity<Unit, List<String>>

    suspend fun updateChat(
        id: Long,
        title: String? = null,
        chatMembers: List<Long>? = null
    ): ResponseEntity<ChatEntity, List<String>>

    suspend fun markMessageAsRead(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun markMessageAsReadLocal(id: Long)

    val messagesStream: LiveData<MessageSocketEntity?>
    fun connectToChannel(groupId: Long)
    fun disconnect()
    fun insertMessage(message: MessageEntity, chatId: Long)
    suspend fun removeMessageRemote(messageId: Long): ResponseEntity<Unit, List<String>>
    fun removeMessageLocal(id: Long)
    fun removeBlockedUser(user: BlockedUserEntity)
    fun addBlockedUser(user: BlockedUserEntity)
    suspend fun unblockUser(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun blockUser(userId: Long): ResponseEntity<Unit, List<String>>
    fun getBlockedUsers(blockedUsersDTO: BlockedUsersDTO): LiveData<PagedList<BlockedUserEntity>>
    suspend fun getUnreadMessageCount(): Long

    fun loadChats(
        coroutineScope: CoroutineScope,
        request: ChatListDTO
    ): LiveData<com.doneit.ascend.domain.use_case.PagedList<ChatEntity>>

    suspend fun getChannelDetails(
        coroutineScope: CoroutineScope,
        channelId: Long
    ): ResponseEntity<ChatEntity, List<String>>

    suspend fun createChannel(coroutineScope: CoroutineScope, newChannelDTO: NewChannelDTO): ResponseEntity<ChatEntity, List<String>>

    suspend fun updateChannel(coroutineScope: CoroutineScope, channelId: Long, newChannelDTO: NewChannelDTO): ResponseEntity<ChatEntity, List<String>>
}