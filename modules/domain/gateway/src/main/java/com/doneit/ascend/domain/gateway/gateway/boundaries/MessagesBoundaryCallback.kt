package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository
import kotlinx.coroutines.CoroutineScope

class MessagesBoundaryCallback (
    scope: CoroutineScope,
    private val local: IMyChatsRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository,
    private val messageListModel: MessageListDTO,
    private val chatId: Long
) : BaseBoundary<MessageEntity>(scope) {
    override suspend fun fetchPage() {
        val response = remote.getMessages(chatId, messageListModel.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.messages?.map { it.toEntity() }

            model?.let {
                val loadedCount = model.size
                val remoteCount = response.successModel!!.count

                receivedItems(loadedCount, remoteCount)

                local.insertAllMessages(model.map { it.toLocal() })
            }
        }
    }
}