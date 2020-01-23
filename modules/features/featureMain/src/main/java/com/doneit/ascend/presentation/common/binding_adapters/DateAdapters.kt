package com.doneit.ascend.presentation.common.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:fullDate")
fun TextView.setDate(dateTime: Date?) {
    dateTime?.let {
        try {
            val formatter = SimpleDateFormat("dd MMM YYYY hh:mm aa", Locale.getDefault())
            text = formatter.format(dateTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


@BindingAdapter("app:setDate")
fun setDate(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date?) {
    try {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        view.text = formatter.format(dateTime)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:setTime")
fun setTime(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date) {
    try {

        dateTime?.let {
            val sdf = SimpleDateFormat("hh:mm aa")
            view.text = sdf.format(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}