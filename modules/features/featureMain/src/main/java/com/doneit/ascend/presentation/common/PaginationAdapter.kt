package com.doneit.ascend.presentation.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.use_case.PagedList

abstract class PaginationAdapter<T, VH : RecyclerView.ViewHolder>(
    private val itemCallback: DiffUtil.ItemCallback<T>,
    private val diffOffset: Int = 0
) : RecyclerView.Adapter<VH>() {

    protected var currentList: PagedList<T>? = null

    protected open val adapterCallback by lazy {
        OffsetListUpdateCallback(this, diffOffset)
    }

    protected open val doAfterListUpdated: () -> Unit = {}

    open fun submitList(list: PagedList<T>) {
        val current = currentList ?: kotlin.run {
            currentList = list
            notifyDataSetChanged()
            return
        }
        current.lock()
        list.lock()
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = current.size
            override fun getNewListSize() = list.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemCallback.areItemsTheSame(
                    current[oldItemPosition],
                    list[newItemPosition]
                )
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemCallback.areContentsTheSame(
                    current[oldItemPosition],
                    list[newItemPosition]
                )
            }
        }
        DiffUtil.calculateDiff(diffCallback).apply {
            currentList = list
            dispatchUpdatesTo(adapterCallback)
            doAfterListUpdated()
        }
        current.unlock()
        list.unlock()
    }

    fun getItem(position: Int): T {
        return currentList.orEmpty()[position - diffOffset]
    }

    override fun getItemCount(): Int {
        return currentList.orEmpty().size + diffOffset
    }

    protected open class OffsetListUpdateCallback(
        private val adapter: RecyclerView.Adapter<*>,
        private val offset: Int
    ) : ListUpdateCallback {

        open fun offsetPosition(originalPosition: Int): Int {
            return originalPosition + offset
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(offsetPosition(position), count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(offsetPosition(position), count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(offsetPosition(fromPosition), offsetPosition(toPosition))
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(offsetPosition(position), count, payload)
        }
    }
}