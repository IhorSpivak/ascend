package com.doneit.ascend.presentation.main.home.common

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.doneit.ascend.presentation.main.R

@BindingAdapter("app:setAdapter")
fun setAdapter(view: androidx.viewpager.widget.ViewPager, adapter: TabAdapter) {
    view.adapter = adapter
}

@BindingAdapter("app:setImage")
fun setImage(view: AppCompatImageView, url: String?) {

    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_stroke_plus)
        .into(view)
}