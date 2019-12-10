package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntityList
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toQuestionEntityList
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toQuestionList
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IQuestionGateway
import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.source.storage.local.repository.question.IQuestionRepository as LocalRepository
import com.doneit.ascend.source.storage.remote.repository.question.IQuestionRepository as RemoteRepository

internal class QuestionGateway(
    errors: NetworkManager,
    private val remote: RemoteRepository,
    private val local: LocalRepository
) : BaseGateway(errors), IQuestionGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getList(sessionToken: String): ResponseEntity<List<QuestionEntity>, List<String>> {
        return executeRemote { remote.getList() }.toResponseEntity(
            {
                it?.questions?.toEntityList()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getQuestionsList(): List<QuestionEntity> {
        return local.getAll().toQuestionList()
    }

    override suspend fun insert(questions: List<QuestionEntity>) {
        local.insert(questions.toQuestionEntityList())
    }

    override suspend fun deleteAllQuestions() {
        local.removeAll()
    }
}