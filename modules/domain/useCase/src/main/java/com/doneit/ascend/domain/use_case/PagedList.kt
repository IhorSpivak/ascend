package com.doneit.ascend.domain.use_case

import java.util.*

class PagedList<T> internal constructor(
    private val dataSource: DataSource<T>,
    private val threshold: Float
) : LinkedList<T>() {

    private var isLocked = false

    fun lock() {
        isLocked = true
    }

    fun unlock() {
        isLocked = false
    }

    override fun clear() {
        super.clear()
        dataSource.notifyChanged()
    }

    override fun remove(element: T): Boolean {
        return super.remove(element).also { dataSource.notifyChanged() }
    }

    override fun removeAt(index: Int): T {
        return super.removeAt(index).also { dataSource.notifyChanged() }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return super.removeAll(elements).also { dataSource.notifyChanged() }
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex).also { dataSource.notifyChanged() }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return super.addAll(elements).also { dataSource.notifyChanged() }
    }

    override fun add(element: T): Boolean {
        return super.add(element).also { dataSource.notifyChanged() }
    }

    override fun get(index: Int): T {
        if ((index + 1).toFloat() / size >= threshold && !isLocked) {
            dataSource.loadNext()
        }
        return super.get(index)
    }
}