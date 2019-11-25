package com.doneit.ascend.presentation.login.views

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData

@BindingAdapter("app:error")
fun TextView.setText(text: LiveData<Int?>?) {
    if(text != null && text.value != null){
        this.text = resources.getString(text.value!!)
    } else {
        this.text = ""
    }
}
