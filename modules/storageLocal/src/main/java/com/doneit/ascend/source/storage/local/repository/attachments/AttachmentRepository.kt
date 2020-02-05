package com.doneit.ascend.source.storage.local.repository.attachments

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.AttachmentLocal

internal class AttachmentRepository(
    private val dao: AttachmentDao
) : IAttachmentRepository {
    override fun getAttachmentList(): DataSource.Factory<Int, AttachmentLocal> {
        return dao.getAll()
    }

    override suspend fun getAttachmentById(id: Long): AttachmentLocal? {
        return dao.getById(id)
    }

    override suspend fun insertAll(attachments: List<AttachmentLocal>) {
        dao.insertAll(attachments)
    }

    override suspend fun update(attachment: AttachmentLocal) {
        dao.update(attachment)
    }

    override suspend fun remove(attachment: AttachmentLocal) {
        dao.remove(attachment)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }

}