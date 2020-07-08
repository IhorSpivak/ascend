package com.doneit.ascend.presentation.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.use_case.PagedList

abstract class PaginationAdapter<T, VH : RecyclerView.ViewHolder>(
    private val itemCallback: DiffUtil.ItemCallback<T>,
    private val diffOffset: Int = 0
) : RecyclerView.Adapter<VH>() {

    protected var currentList: PagedList<T>? = null

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
            dispatchUpdatesTo(this@PaginationAdapter)
        }
        current.unlock()
        list.unlock()
    }

    override fun getItemCount(): Int {
        return currentList?.size ?: 0
    }
}