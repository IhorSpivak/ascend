package com.doneit.ascend.presentation.main.home.community_feed.create_post.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Attachment

abstract class AttachmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(attachment: Attachment)
}