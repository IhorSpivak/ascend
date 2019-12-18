package com.doneit.ascend.presentation.common

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("app:html")
fun TextView.setAdapter(source: String?) {
    if(source != null) {
        text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}