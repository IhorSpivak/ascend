package com.doneit.ascend.presentation.main.create_group.create_support_group.common

import android.view.View
import kotlinx.android.synthetic.main.template_meeting_format.view.*

class FormatHolder(private val itemView: View) {
    fun bind(title: String) {
        itemView.apply {
            tvTitle.text = title
        }
    }
}