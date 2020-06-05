package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListDTO
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.gateway.common.mapper.Constants
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.MMBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.IMasterMindGateway
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.Executors

internal class MasterMindGateway(
    errors: NetworkManager,
    private val local: com.doneit.ascend.source.storage.local.repository.master_minds.IMasterMindRepository,
    private val remote: IMasterMindRepository
) : BaseGateway(errors), IMasterMindGateway {
    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getMasterMindsList(listRequest: MasterMindListDTO): ResponseEntity<List<MasterMindEntity>, List<String>> {
        return executeRemote { remote.getMasterMindsList(listRequest.toRequest()) }.toResponseEntity(
            {
                it?.users?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }

    override fun getMasterMindsPagedList(listRequest: MasterMindListDTO) =
        liveData<PagedList<MasterMindEntity>> {
            local.removeAll()

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(listRequest.perPage ?: Constants.PER_PAGE)
                .build()

            val factory = local.getMMList(listRequest.toLocal()).map { it.toEntity() }

            val boundary = MMBoundaryCallback(
                GlobalScope,
                local,
                remote,
                listRequest
            )

            emitSource(
                LivePagedListBuilder<Int, MasterMindEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override fun getProfile(id: Long) = liveData<UserEntity?> {
        val liveUser = MutableLiveData<UserEntity>()
        emitSource(liveUser)
        executeRemote { remote.getMMProfile(id) }.toResponseEntity(
            {
                liveUser.postValue(it?.toEntity())
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun follow(userId: Long): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.follow(userId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.getMMById(userId)?.let {
                val user = it.copy(followed = true)
                local.update(user)
            }
        }

        return result
    }

    override suspend fun unfollow(userId: Long): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.unfollow(userId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            local.getMMById(userId)?.let {
                val user = it.copy(followed = false)
                local.update(user)
            }
        }

        return res
    }

    override suspend fun setRating(userId: Long, rating: Int): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.setRatting(userId, rating) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun sendReport(
        userId: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.sendReport(userId, content) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }
}