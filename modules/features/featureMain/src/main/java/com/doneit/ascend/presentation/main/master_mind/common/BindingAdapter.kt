package com.doneit.ascend.presentation.main.master_mind.common

import androidx.databinding.BindingAdapter

@BindingAdapter("app:setAdapter")
fun setAdapter(view: androidx.viewpager.widget.ViewPager, adapter: TabAdapter) {
    view.adapter = adapter
}