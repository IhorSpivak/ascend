package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.MemberListDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyChatsBoundaryCallback(
    scope: CoroutineScope,
    private val local: IMyChatsRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository,
    private val chatListModel: ChatListDTO
) : BaseBoundary<ChatEntity>(scope) {


    override suspend fun fetchPage() {
        val response = remote.getMyChats(chatListModel.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {

            val model = response.successModel!!.chats?.map { it.toEntity() }
            model?.let {
                val loadedCount = model.size
                val remoteCount = response.successModel!!.count
                receivedItems(loadedCount, remoteCount)
                coroutineScope {
                    it.forEach {
                        launch {
                            val membersResponse =
                                remote.getMembers(it.id, MemberListDTO(perPage = 50).toRequest(1))
                            if (membersResponse.isSuccessful) {
                                val memberModel =
                                    membersResponse.successModel!!.users?.map { it.toEntity() }
                                it.members = memberModel
                            }
                        }
                    }
                }
                local.insertAll(model.map { it.toLocal() })
            }
        }
    }
}