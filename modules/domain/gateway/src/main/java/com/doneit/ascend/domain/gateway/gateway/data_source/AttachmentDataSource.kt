package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class AttachmentDataSource(
    private val scope: CoroutineScope,
    private val remote: IAttachmentsRepository,
    private val listDTO: AttachmentsListDTO
) : PageKeyedDataSource<Int, AttachmentEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AttachmentEntity>
    ) {
        scope.launch {
            try {
                val list = arrayListOf<AttachmentEntity>()
                list.add(AttachmentEntity(1, "file", 22, "http", 1,
                    1, false, AttachmentType.FILE, Date(1980,2,2), Date(1981,2,3)))
                list.add(AttachmentEntity(2, "image", 22, "https://ourprince.files.wordpress.com/2010/11/link.jpg", 1,
                    1, false, AttachmentType.IMAGE, Date(1980,2,2), Date(1981,2,3)))
                list.add(AttachmentEntity(2, "link", 22, "https://www.google.com/imgres?imgurl=http%3A%2F%2Fourprince.files.wordpress.com%2F2010%2F11%2Flink.jpg&imgrefurl=https%3A%2F%2Ftwisted-gamers.net%2Fforums%2Ftopic%2F8292-how-you-think-about-a-gfx-section%2F&docid=yFAWOiMqkzFvcM&tbnid=IjSVEcHOjjX53M%3A&vet=10ahUKEwiNh6efnbjnAhXj-SoKHYjnDjMQMwhBKAAwAA..i&w=600&h=626&bih=877&biw=1860&q=jpeg%20link&ved=0ahUKEwiNh6efnbjnAhXj-SoKHYjnDjMQMwhBKAAwAA&iact=mrc&uact=8", 1,
                    1, false, AttachmentType.LINK, Date(1980,2,2), Date(1981,2,3)))
                val page = 1

                val groups = remote.getAttachmentsList(listDTO.toRequest(page)).toResponseEntity(
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
                val list = arrayListOf<AttachmentEntity>()
                list.add(AttachmentEntity(1, "dd", 22, "http", 1,
                    1, false, AttachmentType.FILE, Date(1980,2,2), Date(1981,2,3)))
                list.add(AttachmentEntity(2, "dd2", 22, "http", 1,
                    1, false, AttachmentType.FILE, Date(1980,2,2), Date(1981,2,3)))
                val page = params.key

                val groups =
                    remote.getAttachmentsList(listDTO.toRequest(page)).toResponseEntity(
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
