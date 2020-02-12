package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.attachments.IAttachmentRepository
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import kotlinx.coroutines.CoroutineScope
import java.util.*

class AttachmentBoundaryCallback(
    scope: CoroutineScope,
    private val local: IAttachmentRepository,
    private val remote: IAttachmentsRepository,
    private val masterMindListDTO: AttachmentsListDTO
) : BaseBoundary<AttachmentEntity>(scope) {

    override suspend fun fetchPage() {
        val response = remote.getAttachmentsList(masterMindListDTO.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.attachments.map { it.toEntity() }

            val loadedCount = model.size
            val remoteCount = response.successModel!!.count

            receivedItems(loadedCount, remoteCount)

            //TODO: remove
            val list = arrayListOf<AttachmentEntity>()
            list.add(
                AttachmentEntity(
                    1, "file", 22, "http", 1,
                    1, false, AttachmentType.FILE, Date(1980, 2, 2), Date(1981, 2, 3)
                )
            )
            list.add(
                AttachmentEntity(
                    2, "image", 22, "https://ourprince.files.wordpress.com/2010/11/link.jpg", 1,
                    1, false, AttachmentType.IMAGE, Date(1980, 2, 2), Date(1981, 2, 3)
                )
            )
            list.add(
                AttachmentEntity(
                    3,
                    "link",
                    22,
                    "https://www.google.com/imgres?imgurl=http%3A%2F%2Fourprince.files.wordpress.com%2F2010%2F11%2Flink.jpg&imgrefurl=https%3A%2F%2Ftwisted-gamers.net%2Fforums%2Ftopic%2F8292-how-you-think-about-a-gfx-section%2F&docid=yFAWOiMqkzFvcM&tbnid=IjSVEcHOjjX53M%3A&vet=10ahUKEwiNh6efnbjnAhXj-SoKHYjnDjMQMwhBKAAwAA..i&w=600&h=626&bih=877&biw=1860&q=jpeg%20link&ved=0ahUKEwiNh6efnbjnAhXj-SoKHYjnDjMQMwhBKAAwAA&iact=mrc&uact=8",
                    1,
                    1,
                    false,
                    AttachmentType.LINK,
                    Date(1980, 2, 2),
                    Date(1981, 2, 3)
                )
            )
            //

            local.insertAll(model.map { it.toLocal() })
        }
    }
}