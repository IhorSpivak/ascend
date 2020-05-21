package com.doneit.ascend.domain.gateway.gateway.boundaries

import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseBoundary<T>(
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<T>() {

    protected var remoteCount = 0
    protected var loadedCount = 0
    protected var pageIndexToLoad = 1
    protected var localDifference = 1

    fun loadInitial() {
        loadPage()
    }

    //It isn't used because of unsuitable invoke cases
    override fun onZeroItemsLoaded() {
    }

    override fun onItemAtEndLoaded(itemAtEnd: T) {
        if (loadedCount < remoteCount) {
            loadPage()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: T) {
        if (localDifference == 0) {
            loadPage()
        }
    }

    protected fun loadPage() {
        scope.launch(Dispatchers.IO) {
            fetchPage()
        }
    }

    abstract suspend fun fetchPage()

    protected fun receivedItems(loaded: Int, from: Int, saved: Int = 1) {
        remoteCount = from
        pageIndexToLoad += 1
        loadedCount += loaded
        localDifference = saved
    }
}