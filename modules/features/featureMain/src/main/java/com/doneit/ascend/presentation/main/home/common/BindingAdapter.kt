package com.doneit.ascend.presentation.main.home.common

import androidx.databinding.BindingAdapter

@BindingAdapter("app:setAdapter")
fun setAdapter(view: androidx.viewpager.widget.ViewPager, adapter: TabAdapter) {
    view.adapter = adapter
}