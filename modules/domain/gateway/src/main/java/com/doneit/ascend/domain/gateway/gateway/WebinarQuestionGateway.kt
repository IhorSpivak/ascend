package com.doneit.ascend.domain.gateway.gateway

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.WebinarQuestionCallBack
import com.doneit.ascend.domain.use_case.gateway.IWebinarQuestionGateway
import com.doneit.ascend.source.storage.local.repository.webinar_question.IWebinarQuestionRepository
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.repository.group.questions_socket.IQuestionSocketRepository
import com.doneit.ascend.source.storage.remote.repository.group.webinar_questions.IWebinarQuestionsRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class WebinarQuestionGateway(
    private val remote: IWebinarQuestionsRepository,
    private val local: IWebinarQuestionRepository,
    private val remoteSocket: IQuestionSocketRepository,
    private val accountManager: AccountManager,
    private val packageName: String,
    errors: NetworkManager
) : BaseGateway(errors), IWebinarQuestionGateway {
    override fun getQuestions(
        groupId: Long,
        request: WebinarQuestionDTO
    ): LiveData<PagedList<WebinarQuestionEntity>> {
        return liveData<PagedList<WebinarQuestionEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 10)
                .build()
            val factory = local.getAll().map { it.toEntity() }

            val boundary = WebinarQuestionCallBack(
                GlobalScope,
                local,
                remote,
                request,
                groupId
            )

            emitSource(
                LivePagedListBuilder<Int, WebinarQuestionEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }
    }

    override suspend fun create(
        groupId: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        return executeRemote {
            remote.createQuestion(
                groupId, content
            )
        }.toResponseEntity(
            {
                Unit
            }, {
                it?.errors
            }
        )
    }

    override suspend fun update(id: Long, content: String): ResponseEntity<Unit, List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        TODO("Not yet implemented")
    }

    override val questionStream: LiveData<QuestionSocketEntity?> =
        remoteSocket.questionStream.map { it?.toEntity() }

    override
    fun connectToChannel(groupId: Long) {
        GlobalScope.launch {

            val accounts = accountManager.getAccountsByType(packageName)

            if (accounts.isNotEmpty()) {
                val account = accounts[0]
                val token = accountManager.blockingGetAuthToken(account, "Bearer", false)
                val cookies =
                    GroupSocketCookies(
                        token,
                        groupId
                    )
                remoteSocket.connect(cookies)
            }
        }
    }

    override fun disconnect() {
        remoteSocket.disconnect()
    }

    override fun insertQuestion(question: WebinarQuestionEntity) {
        GlobalScope.launch {
            local.insert(question.toLocal())
        }
    }

    override fun removeQuestionLocal(id: Long) {

    }

}