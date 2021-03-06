package com.doneit.ascend.domain.use_case.interactor.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.use_case.gateway.IMyChatGateway
import kotlinx.coroutines.CoroutineScope

class ChatInteractor(
    private val chatGateway: IMyChatGateway
) : ChatUseCase {
    override fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>> {
        return chatGateway.getMyChatListLive(request)
    }

    override fun getChatList(request: ChatListDTO): PagedList<ChatEntity> {
        return chatGateway.getChatsList(request)
    }

    override fun getMessageList(
        chatId: Long,
        request: MessageListDTO
    ): LiveData<PagedList<MessageEntity>> {
        return chatGateway.getMessages(chatId, request)
    }

    override suspend fun getChatDetails(
        id: Long,
        saveToLocal: Boolean
    ): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.getChatDetails(id, saveToLocal)
    }

    override fun getMemberListLive(
        chatId: Long,
        request: MemberListDTO
    ): LiveData<PagedList<MemberEntity>> {
        return chatGateway.getMembers(chatId, request)
    }

    override suspend fun getMembersList(
        chatId: Long,
        request: MemberListDTO
    ): ResponseEntity<List<MemberEntity>, List<String>> {
        return chatGateway.getMemberList(chatId, request)
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

    override suspend fun removeUser(
        chatId: Long,
        userId: Long
    ): ResponseEntity<Unit, List<String>> {
        return chatGateway.removeUser(chatId, userId)
    }

    override suspend fun updateChat(
        id: Long,
        title: String?,
        chatMembers: List<Long>?
    ): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.updateChat(id, title, chatMembers)
    }

    override suspend fun markMessageAsRead(id: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.markMessageAsRead(id)
    }

    override suspend fun markMessageAsReadLocal(id: Long) {
        return chatGateway.markMessageAsReadLocal(id)
    }

    override val messagesStream = chatGateway.messagesStream

    override fun connectToChannel(groupId: Long) {
        chatGateway.connectToChannel(groupId)
    }

    override fun disconnect() {
        chatGateway.disconnect()
    }

    override fun insertMessage(message: MessageEntity, chatId: Long) {
        chatGateway.insertMessage(message, chatId)
    }

    override suspend fun removeMessageRemote(messageId: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.removeMessageRemote(messageId)
    }

    override fun removeMessageLocal(id: Long) {
        chatGateway.removeMessageLocal(id)
    }

    override fun removeBlockedUser(user: BlockedUserEntity) {
        chatGateway.removeBlockedUser(user)
    }

    override fun addBlockedUser(user: BlockedUserEntity) {
        chatGateway.addBlockedUser(user)
    }

    override suspend fun unblockUser(userId: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.unblockUser(userId)
    }

    override suspend fun blockUser(userId: Long): ResponseEntity<Unit, List<String>> {
        return chatGateway.blockUser(userId)
    }

    override fun getBlockedUsers(blockedUsersDTO: BlockedUsersDTO): LiveData<PagedList<BlockedUserEntity>> {
        return chatGateway.getBlockedUsersLive(blockedUsersDTO)
    }

    override suspend fun getUnreadMessageCount(): Long {
        return chatGateway.getUnreadMessageCount()
    }

    override fun loadChats(
        coroutineScope: CoroutineScope,
        request: ChatListDTO
    ): LiveData<com.doneit.ascend.domain.use_case.PagedList<ChatEntity>> {
        return chatGateway.loadChats(coroutineScope, request)
    }

    override suspend fun getChannelDetails(
        coroutineScope: CoroutineScope,
        channelId: Long
    ): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.getChannelDetails(coroutineScope, channelId)
    }

    override suspend fun createChannel(
        coroutineScope: CoroutineScope,
        newChannelDTO: NewChannelDTO
    ): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.createChannel(coroutineScope, newChannelDTO)
    }

    override suspend fun updateChannel(
        coroutineScope: CoroutineScope,
        channelId: Long,
        newChannelDTO: NewChannelDTO
    ): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.updateChannel(coroutineScope, channelId, newChannelDTO)
    }

    override suspend fun joinChannel(coroutineScope: CoroutineScope, channelId: Long): ResponseEntity<ChatEntity, List<String>> {
        return chatGateway.joinChannel(coroutineScope, channelId)
    }
}