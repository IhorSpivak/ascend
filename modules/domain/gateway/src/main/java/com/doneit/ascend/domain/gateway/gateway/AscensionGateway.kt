package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.domain.entity.dto.ascension_plan.FilterDTO
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.AscensionDataSource
import com.doneit.ascend.domain.use_case.gateway.IAscensionGateway
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.Executors

class AscensionGateway(
    errors: NetworkManager
) : BaseGateway(errors), IAscensionGateway {
    override fun getListPaged(filter: FilterDTO): LiveData<PagedList<AscensionEntity>> =
        liveData {

            val config = getDefaultPagedConfig(filter)
            val factory = object : DataSource.Factory<Int, AscensionEntity>() {
                override fun create(): DataSource<Int, AscensionEntity> {
                    return AscensionDataSource(
                        GlobalScope,
                        //remote,
                        filter
                    )
                }
            }

            emitSource(
                LivePagedListBuilder<Int, AscensionEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .build()
            )
        }
}