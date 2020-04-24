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

interface ChatUseCase {
    fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>>
    fun getMessageList(chatId: Long, request: MessageListDTO): LiveData<PagedList<MessageEntity>>

    @Deprecated(
        "don't use this method as it doesn't provide chat id in database",
        ReplaceWith("field members in chatEntity"),
        DeprecationLevel.WARNING
    )
    fun getMemberList(chatId: Long, request: MemberListDTO): LiveData<PagedList<MemberEntity>>
    suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>>
    suspend fun leave(id: Long): ResponseEntity<Unit, List<String>>
    suspend fun updateChat(
        id: Long,
        title: String? = null,
        chatMembers: List<Long>? = null
    ): ResponseEntity<ChatEntity, List<String>>

    val messagesStream: LiveData<MessageSocketEntity?>
    fun connectToChannel(groupId: Long)
    fun disconnect()
    fun insertMessage(message: MessageEntity, chatId: Long)
    suspend fun removeMessageRemote(messageId: Long): ResponseEntity<Unit, List<String>>
    fun removeMessageLocal(message: MessageEntity)
    fun removeBlockedUser(user: BlockedUserEntity)
    fun addBlockedUser(user: BlockedUserEntity)
    suspend fun unblockUser(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun blockUser(userId: Long): ResponseEntity<Unit, List<String>>
    fun getBlockedUsers(blockedUsersDTO: BlockedUsersDTO): LiveData<PagedList<BlockedUserEntity>>
}