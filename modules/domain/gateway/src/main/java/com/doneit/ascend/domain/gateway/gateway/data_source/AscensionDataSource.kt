package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.domain.entity.ascension.GoalEntity
import com.doneit.ascend.domain.entity.ascension.SpiritualStepEntity
import com.doneit.ascend.domain.entity.dto.ascension_plan.FilterDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class AscensionDataSource(
    private val scope: CoroutineScope,
    //private val remote: IAttachmentsRepository, todo
    private val listDTO: FilterDTO
) : PageKeyedDataSource<Int, AscensionEntity>() {

    private val mock = listOf(
        SpiritualStepEntity(1, Date(), "t1", "15", "1"),
        GoalEntity(2, Date(), "t2", 20, 2, "non"),
        AscensionEntity(4, Date()),
        SpiritualStepEntity(3, Date(), "t2", "25", "3")
    )

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AscensionEntity>
    ) {
        scope.launch {
            try {


                val page = 1

                /*val groups = remote.getAttachmentsList(listDTO.toRequest(page)).toResponseEntity(
                    {
                        it?.attachments?.map { attachment -> attachment.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )*/

                //if (groups.isSuccessful) {
                callback.onResult(mock, null, page + 1)
                //}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AscensionEntity>) {
        scope.launch {
            try {
                val page = params.key

                /*val groups =
                    remote.getAttachmentsList(listDTO.toRequest(page)).toResponseEntity(
                        {
                            it?.attachments?.map { attachment -> attachment.toEntity() }
                        },
                        {
                            it?.errors
                        }
                    )*/

                //if (groups.isSuccessful) {
                callback.onResult(mock, page + 1)
                //}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AscensionEntity>) {
    }
}