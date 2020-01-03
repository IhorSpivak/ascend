package com.doneit.ascend.presentation.profile.rating.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.RateEntity


class RatingAdapter: PagedListAdapter<RateEntity, RatingViewHolder>(RatingDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        return RatingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.hashCode().toLong()
    }
}