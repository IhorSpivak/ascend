package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.AttachmentBoundaryCallback
import com.doneit.ascend.domain.gateway.gateway.data_source.AttachmentDataSource
import com.doneit.ascend.domain.use_case.gateway.IAttachmentGateway
import com.doneit.ascend.source.storage.local.repository.attachments.IAttachmentRepository
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.Executors

internal class AttachmentGateway(
    errors: NetworkManager,
    private val local: IAttachmentRepository,
    private val remote: IAttachmentsRepository
) : BaseGateway(errors), IAttachmentGateway {

    override suspend fun getAttachments(listModel: AttachmentsListModel): PagedList<AttachmentEntity> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(listModel.perPage ?: 10)
            .build()

        val dataSource = AttachmentDataSource(
            GlobalScope,
            remote,
            listModel
        )
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, AttachmentEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }

    override fun getAttachmentsPagedList(listModel: AttachmentsListModel) =
        liveData<PagedList<AttachmentEntity>> {
            local.removeAll()

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(listModel.perPage ?: 10)
                .build()

            val factory = local.getAttachmentList().map { it.toEntity() }

            val boundary = AttachmentBoundaryCallback(
                GlobalScope,
                local,
                remote,
                listModel
            )

            emitSource(
                LivePagedListBuilder<Int, AttachmentEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }


    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.deleteAttachment(id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if(res.isSuccessful) {
            local.getAttachmentById(id)?.let {
                local.remove(it)
            }
        }
        return res
    }
}