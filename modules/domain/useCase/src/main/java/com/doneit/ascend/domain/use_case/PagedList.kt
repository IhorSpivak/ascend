package com.doneit.ascend.domain.use_case

import java.util.*

class PagedList<T> internal constructor(
    private val dataSource: DataSource<T>,
    private val threshold: Float
) : LinkedList<T>() {

    private var isLocked = false

    internal constructor(dataSource: DataSource<T>, threshold: Float, list: List<T>) : this(
        dataSource,
        threshold
    ) {
        lock()
        addAll(list)
        unlock()
    }

    fun lock() {
        isLocked = true
    }

    fun unlock() {
        isLocked = false
    }

    private fun notifyChanged() {
        if (!isLocked) {
            dataSource.notifyChanged()
        }
    }

    override fun clear() {
        super.clear()
        notifyChanged()
    }

    override fun remove(element: T): Boolean {
        return super.remove(element).also { notifyChanged() }
    }

    override fun removeAt(index: Int): T {
        return super.removeAt(index).also { notifyChanged() }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return super.removeAll(elements).also { notifyChanged() }
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex).also { notifyChanged() }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return super.addAll(elements).also { notifyChanged() }
    }

    override fun add(element: T): Boolean {
        return super.add(element).also { notifyChanged() }
    }

    override fun set(index: Int, element: T): T {
        return dataSource.set(index, element)
    }

    override fun add(index: Int, element: T) {
        dataSource.insert(index, element)
    }

    override fun get(index: Int): T {
        if ((index + 1).toFloat() / size >= threshold && !isLocked) {
            dataSource.loadNext()
        }
        return super.get(index)
    }
}