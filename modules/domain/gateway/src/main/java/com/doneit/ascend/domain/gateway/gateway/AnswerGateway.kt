package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntityList
import com.doneit.ascend.source.storage.remote.repository.answer.IAnswerRepository
import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway

internal class AnswerGateway(
    errors: NetworkManager,
    private val remote: IAnswerRepository
) : BaseGateway(errors), IAnswerGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun createAnswers(
        sessionToken: String,
        answers: List<AnswerEntity>
    ): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.createAnswers(sessionToken, answers.toEntityList()) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }
}