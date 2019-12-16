package com.doneit.ascend.presentation.main.search.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: SearchAdapter,
    entities: LiveData<PagedList<SearchEntity>>?
) {

    if (view.adapter is SearchAdapter) {
        entities?.value?.let {
            (view.adapter as SearchAdapter).submitList(it)

            return
        }
    }

    view.adapter = adapter
}