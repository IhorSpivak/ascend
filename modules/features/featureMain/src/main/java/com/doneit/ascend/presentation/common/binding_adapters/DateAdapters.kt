package com.doneit.ascend.presentation.common.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.doneit.ascend.presentation.utils.extensions.getTimeFormat
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import java.util.*

@BindingAdapter("app:fullDate")
fun TextView.setDate(dateTime: Date?) {
    dateTime?.let {
        try {
            val formatter = "dd MMM YYYY hh:mm aa".toDefaultFormatter()
            text = formatter.format(dateTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


@BindingAdapter("app:setDate")
fun setDate(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date?) {
    try {
        val formatter = "d MMM".toDefaultFormatter()
        view.text = formatter.format(dateTime)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:setTime")
fun setTime(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date) {
    try {
        dateTime?.let {
            view.text = view.context.getTimeFormat().format(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}