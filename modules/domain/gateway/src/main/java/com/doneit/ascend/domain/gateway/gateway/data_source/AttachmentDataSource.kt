package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AttachmentDataSource(
    private val scope: CoroutineScope,
    private val remote: IAttachmentsRepository,
    private val listModel: AttachmentsListModel
) : PageKeyedDataSource<Int, AttachmentEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AttachmentEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val groups = remote.getAttachmentsList(listModel.toRequest(page)).toResponseEntity(
                    {
                        it?.attachments?.map { attachment -> attachment.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AttachmentEntity>) {
        scope.launch {
            try {

                val page = params.key

                val groups =
                    remote.getAttachmentsList(listModel.toRequest(page)).toResponseEntity(
                        {
                            it?.attachments?.map { attachment -> attachment.toEntity() }
                        },
                        {
                            it?.errors
                        }
                    )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AttachmentEntity>) {
    }
}