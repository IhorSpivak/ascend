package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway
import com.doneit.ascend.source.storage.local.repository.question.IQuestionRepository
import com.doneit.ascend.source.storage.local.repository.user.IUserRepository
import com.doneit.ascend.source.storage.remote.repository.answer.IAnswerRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class AnswerGateway(
    errors: NetworkManager,
    private val local: IUserRepository,
    private val questionsLocal: IQuestionRepository,
    private val remote: IAnswerRepository
) : BaseGateway(errors), IAnswerGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun createAnswers(
        answers: AnswersDTO
    ): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.createAnswers(answers.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            val user = local.getFirstUser()
            user?.let {
                local.remove()
                local.insert(user.copy(community = answers.community, unansweredQuestionsCount = 0))
            }
            questionsLocal.removeAll()
        }

        return result
    }
}