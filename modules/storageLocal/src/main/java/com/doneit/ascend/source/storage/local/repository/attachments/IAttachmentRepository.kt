package com.doneit.ascend.source.storage.local.repository.attachments

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.AttachmentLocal

interface IAttachmentRepository {
    fun getAttachmentList(): DataSource.Factory<Int, AttachmentLocal>
    suspend fun getAttachmentById(id: Long): AttachmentLocal?
    suspend fun insertAll(attachments: List<AttachmentLocal>)
    suspend fun update(attachment: AttachmentLocal)
    suspend fun remove(attachment: AttachmentLocal)
    suspend fun removeAll()
}