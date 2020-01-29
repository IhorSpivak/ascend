package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.AttachmentDataSource
import com.doneit.ascend.domain.use_case.gateway.IAttachmentGateway
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope

internal class AttachmentGateway(
    errors: NetworkManager,
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
}