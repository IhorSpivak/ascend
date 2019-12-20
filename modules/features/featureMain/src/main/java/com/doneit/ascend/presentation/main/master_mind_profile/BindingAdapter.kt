package com.doneit.ascend.presentation.main.master_mind_profile

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:visibilityByData")
fun setVisibilityByData(
    view: androidx.appcompat.widget.AppCompatTextView,
    data: String?
) {
    view.visibility = if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
}