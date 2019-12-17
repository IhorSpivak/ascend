package com.doneit.ascend.presentation.main.home.common

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.doneit.ascend.presentation.main.R

@BindingAdapter("app:setAdapter")
fun setAdapter(view: androidx.viewpager.widget.ViewPager, adapter: TabAdapter) {
    view.adapter = adapter
}

@BindingAdapter("app:setImage", "app:placeholder", requireAll = false)
fun setImage(view: AppCompatImageView, url: String?, placeholder: Drawable?) {

    Glide.with(view)
        .load(url)
        .placeholder(placeholder)
        .into(view)
}