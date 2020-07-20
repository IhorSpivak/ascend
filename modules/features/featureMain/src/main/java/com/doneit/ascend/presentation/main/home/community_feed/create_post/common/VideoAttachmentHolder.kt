package com.doneit.ascend.presentation.main.home.community_feed.create_post.common

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemGroupAttachmentVideoBinding
import com.doneit.ascend.presentation.utils.extensions.getDurationBreakdown
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoAttachmentHolder(
    itemView: View,
    private val onDeleteClick: (Int) -> Unit
) : AttachmentHolder(itemView) {

    private val binding: ListItemGroupAttachmentVideoBinding = DataBindingUtil.bind(itemView)!!
    private var holderScope = MainScope()

    private suspend fun runObserverTask() {
        while (true) {
            binding.apply {
                duration.text = ((pvPlayer.player?.duration ?: 0) -
                        (pvPlayer.player?.currentPosition ?: 0))
                    .coerceIn(0..Long.MAX_VALUE)
                    .getDurationBreakdown()
            }
            delay(1000)
        }
    }

    override fun bind(attachment: Attachment) {
        holderScope.cancel()
        holderScope = MainScope()
        binding.apply {
            val player = SimpleExoPlayer.Builder(itemView.context)
                .build()
            pvPlayer.player = player
            player.playWhenReady = false
            player.prepare(createMediaSource(itemView.context, attachment.url))
            holderScope.launch {
                runObserverTask()
            }
            btnDelete.setOnClickListener { onDeleteClick(adapterPosition) }
        }
    }

    private fun createMediaSource(context: Context, url: String): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.getString(R.string.app_name))
            )
        ).createMediaSource(Uri.parse(url))
    }

    override fun clear() {
        holderScope.cancel()
    }

    companion object {
        fun create(parent: ViewGroup, onDeleteClick: (Int) -> Unit): VideoAttachmentHolder {
            return VideoAttachmentHolder(
                DataBindingUtil.inflate<ListItemGroupAttachmentVideoBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_group_attachment_video,
                    parent,
                    false
                ).root,
                onDeleteClick
            )
        }
    }
}