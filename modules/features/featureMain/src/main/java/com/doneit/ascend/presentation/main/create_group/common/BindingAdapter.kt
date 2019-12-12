package com.doneit.ascend.presentation.main.create_group.common

import android.net.Uri
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData

@BindingAdapter("app:setAdapter", "app:items")
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: ParticipantAdapter,
    items: LiveData<List<String>>
) {
    if (view.adapter is ParticipantAdapter) {
        items.value?.let {
            (view.adapter as ParticipantAdapter).update(it)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setParticipantsVisibility")
fun setParticipantsVisibility(
    view: androidx.recyclerview.widget.RecyclerView,
    items: LiveData<List<String>>
) {
    view.visibility = if (items.value != null && items.value?.isNotEmpty() == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:path")
fun AppCompatImageView.setImageUri(path: String?) {
    setImageURI(null)//in order to force image update
    path?.let {
        setImageURI(Uri.parse(it))
    }
}