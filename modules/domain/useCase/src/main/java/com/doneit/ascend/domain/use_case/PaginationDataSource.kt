package com.doneit.ascend.domain.use_case

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class PaginationDataSource<T> private constructor(
    private val coroutineScope: CoroutineScope,
    private val limit: Int,
    private val offset: Int,
    private val remote: PaginationSourceRemote<T>?,
    private val local: PaginationSourceLocal<T>?
) : LiveData<PagedList<T>>(),
    DataSource<T> {

    private var endReached = false
    private var page = offset / limit + 1
    private val dataContainer = PagedList(this, 0.9f)
    private val isLoading = AtomicBoolean(false)

    init {
        postValue(dataContainer)
        loadNext()
    }

    override fun loadNext() {
        if ((remote == null && local == null) || endReached) return
        coroutineScope.launch(Dispatchers.IO) {
            if (!isLoading.get() && !endReached) {
                isLoading.compareAndSet(false, true)
                remote?.loadData(page, limit)?.let {
                    endReached = it.size < limit
                    page += 1
                    local?.save(it)
                    dataContainer.addAll(it)
                }
                isLoading.compareAndSet(true, false)
            }
        }
    }

    override fun notifyChanged() {
        postValue(dataContainer)
    }

    class Builder<T> {
        private var coroutineScope: CoroutineScope = GlobalScope
        private var offset = BASE_OFFSET
        private var limit = BASE_LIMIT
        private var remote: PaginationSourceRemote<T>? = null
        private var local: PaginationSourceLocal<T>? = null

        fun coroutineScope(coroutineScope: CoroutineScope): Builder<T> {
            this.coroutineScope = coroutineScope
            return this
        }

        fun offset(offset: Int): Builder<T> {
            this.offset = offset
            return this
        }

        fun pageLimit(limit: Int): Builder<T> {
            this.limit = limit
            return this
        }

        fun remoteSource(remote: PaginationSourceRemote<T>): Builder<T> {
            this.remote = remote
            return this
        }

        fun localSource(local: PaginationSourceLocal<T>): Builder<T> {
            this.local = local
            return this
        }

        fun build(): PaginationDataSource<T> {
            return PaginationDataSource(
                coroutineScope = coroutineScope,
                limit = limit,
                offset = offset,
                remote = remote,
                local = local
            )
        }
    }

    companion object {
        private const val BASE_OFFSET = 0
        private const val BASE_LIMIT = 10
    }
}

interface PaginationSourceRemote<T> {
    suspend fun loadData(page: Int, limit: Int): List<T>?
}

interface PaginationSourceLocal<T> {
    suspend fun loadData(page: Int, limit: Int): List<T>?
    suspend fun save(data: List<T>)
}

interface DataSource<T> {
    fun notifyChanged()
    fun loadNext()
}