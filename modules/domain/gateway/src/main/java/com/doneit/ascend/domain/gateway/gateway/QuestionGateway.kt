package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntityList
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toQuestionEntityList
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

    override suspend fun getList(sessionToken: String): ResponseEntity<QuestionListEntity, List<String>> {
        return executeRemote { remote.getList() }.toResponseEntity(
            {
                it?.toEntityList()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getQuestionsList(): QuestionListEntity {
        return local.getAll().toEntity()
    }

    override suspend fun insert(questions: QuestionListEntity) {
        local.insert(questions.toQuestionEntityList())
    }

    override suspend fun deleteAllQuestions() {
        local.removeAll()
    }
}