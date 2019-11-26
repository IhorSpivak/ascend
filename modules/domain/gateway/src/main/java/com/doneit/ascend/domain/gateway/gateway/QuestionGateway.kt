package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntityList
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IQuestionGateway
import com.doneit.ascend.source.storage.remote.repository.question.IQuestionRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class QuestionGateway(
    errors: NetworkManager,
    private val remote: IQuestionRepository
) : BaseGateway(errors), IQuestionGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getList(sessionToken: String): RequestEntity<List<QuestionEntity>, List<String>> {
        return executeRemote { remote.getList(sessionToken) }.toRequestEntity(
            {
                it?.questions?.toEntityList()
            },
            {
                it?.errors
            }
        )
    }
}