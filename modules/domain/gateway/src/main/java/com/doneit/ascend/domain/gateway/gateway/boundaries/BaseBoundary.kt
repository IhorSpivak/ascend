package com.doneit.ascend.domain.gateway.gateway.boundaries

import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseBoundary<T>(
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<T>() {

    private var remoteCount = 0
    private var loadedCount = 0
    protected var pageIndexToLoad = 0

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
    }

    private fun loadPage() {
        scope.launch(Dispatchers.IO) {
            fetchPage()
        }
    }

    abstract suspend fun fetchPage()

    protected fun receivedItems(loaded: Int, from: Int) {
        remoteCount = from
        pageIndexToLoad += 1
        loadedCount += loaded
    }
}