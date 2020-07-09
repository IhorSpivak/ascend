package com.doneit.ascend.presentation.main.home.community_feed.create_post.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType

open class CreatePostAdapter(
    private val onDeleteAttachmentClick: (Int) -> Unit
) : RecyclerView.Adapter<AttachmentHolder>() {

    private var items = arrayListOf<Attachment>()

    fun submitItems(items: List<Attachment>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        return when (viewType) {
            ContentType.IMAGE.ordinal -> ImageAttachmentHolder.create(
                parent,
                onDeleteAttachmentClick
            )
            ContentType.VIDEO.ordinal -> VideoAttachmentHolder.create(
                parent,
                onDeleteAttachmentClick
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].contentType.ordinal
    }


    override fun getItemCount() = items.size
}