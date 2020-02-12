package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.SearchDataSource
import com.doneit.ascend.domain.use_case.gateway.ISearchGateway
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope

internal class SearchGateway(
    errors: NetworkManager,
    private val remoteGroup: IGroupRepository,
    private val remoteMasterMind: IMasterMindRepository
) : BaseGateway(errors), ISearchGateway {
    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getSearchResultPaged(searchRequest: SearchDTO): PagedList<SearchEntity> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(searchRequest.perPage?:10)
            .build()

        val dataSource = SearchDataSource(
            GlobalScope,
            remoteGroup,
            remoteMasterMind,
            searchRequest
        )
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, SearchEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }
}
