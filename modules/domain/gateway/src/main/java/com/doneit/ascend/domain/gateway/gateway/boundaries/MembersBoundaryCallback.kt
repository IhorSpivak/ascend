package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.dto.MemberListDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository
import com.vrgsoft.core.gateway.orZero
import kotlinx.coroutines.CoroutineScope

class MembersBoundaryCallback (
    scope: CoroutineScope,
    private val local: IMyChatsRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository,
    private val memberListDTO: MemberListDTO,
    private val chatId: Long
) : BaseBoundary<MemberEntity>(scope) {
    override suspend fun fetchPage() {
        val response = remote.getMembers(chatId, memberListDTO.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.users?.map { it.toEntity() }

            model?.let {
                val loadedCount = model.size
                val remoteCount = response.successModel!!.count

                receivedItems(loadedCount, remoteCount.orZero())

                local.insertAllMembers(model.map { it.toLocal() })
            }
        }
    }
}