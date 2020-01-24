package com.doneit.ascend.presentation.profile.rating.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateRatingBinding
import com.doneit.ascend.presentation.utils.extensions.toRateDate

class RatingViewHolder (
    private val binding: TemplateRatingBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RateEntity) {
        binding.item = item
        binding.tvDate.text = item.createdAt.toRateDate()
    }

    companion object {
        fun create(parent: ViewGroup): RatingViewHolder {
            val binding: TemplateRatingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_rating,
                parent,
                false
            )

            return RatingViewHolder(binding)
        }
    }
}