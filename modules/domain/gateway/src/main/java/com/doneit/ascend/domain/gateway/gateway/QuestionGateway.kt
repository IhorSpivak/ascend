package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntityList
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
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

    override suspend fun getList(): ResponseEntity<QuestionListEntity, List<String>> {
        return executeRemote { remote.getList() }.toResponseEntity(
            {
                it?.toEntityList()
            },
            {
                it?.errors
            }
        )
    }

    override fun getQuestionsList() = liveData {
        emitSource(MutableLiveData())
        emit(local.getAll()?.toEntity())//todo replace by retrieving LiveData from room

        val res = executeRemote { remote.getList() }.toResponseEntity(
            {
                it?.toEntityList()
            },
            {
                it?.errors
            }
        )

        if(res.isSuccessful) {
            local.insert(res.successModel!!.toLocal())
            emit(res.successModel!!)
        }
    }
}