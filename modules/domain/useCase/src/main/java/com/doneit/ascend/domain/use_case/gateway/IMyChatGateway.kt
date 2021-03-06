package com.doneit.ascend.domain.use_case.gateway

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

interface IMyChatGateway {
    fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>>

    fun getChatsList(request: ChatListDTO): PagedList<ChatEntity>

    suspend fun getChatDetails(
        id: Long,
        saveToLocal: Boolean = true
    ): ResponseEntity<ChatEntity, List<String>>

    fun getMessages(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>

    fun getMembers(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>

    suspend fun getMemberList(
        chatId: Long,
        request: MemberListDTO
    ): ResponseEntity<List<MemberEntity>, List<String>>

    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>>

    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>

    suspend fun markMessageAsRead(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun markMessageAsReadLocal(id: Long)

    suspend fun updateChat(
        id: Long,
        title: String? = null,
        chatMembers: List<Long>? = null
    ): ResponseEntity<ChatEntity, List<String>>

    val messagesStream: LiveData<MessageSocketEntity?>

    fun connectToChannel(id: Long)

    fun disconnect()
    fun insertMessage(message: MessageEntity, chatId: Long)
    fun removeMessageLocal(id: Long)
    fun removeBlockedUser(userEntity: BlockedUserEntity)
    suspend fun removeUser(
        chatId: Long,
        userId: Long
    ): ResponseEntity<Unit, List<String>>

    fun addBlockedUser(userEntity: BlockedUserEntity)
    suspend fun blockUser(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun removeMessageRemote(messageId: Long): ResponseEntity<Unit, List<String>>
    suspend fun unblockUser(userId: Long): ResponseEntity<Unit, List<String>>
    fun getBlockedUsersLive(blockedUsersDTO: BlockedUsersDTO): LiveData<PagedList<BlockedUserEntity>>
    suspend fun getUnreadMessageCount(): Long

    suspend fun getChannelDetails(
        scope: CoroutineScope,
        id: Long
    ): ResponseEntity<ChatEntity, List<String>>

    suspend fun createChannel(
        scope: CoroutineScope,
        request: NewChannelDTO
    ): ResponseEntity<ChatEntity, List<String>>

    suspend fun updateChannel(
        scope: CoroutineScope,
        channelId: Long,
        request: NewChannelDTO
    ): ResponseEntity<ChatEntity, List<String>>

    //experimental:

    fun loadChats(
        scope: CoroutineScope,
        request: ChatListDTO
    ): LiveData<com.doneit.ascend.domain.use_case.PagedList<ChatEntity>>

    suspend fun joinChannel(
        coroutineScope: CoroutineScope,
        channelId: Long
    ): ResponseEntity<ChatEntity, List<String>>
}