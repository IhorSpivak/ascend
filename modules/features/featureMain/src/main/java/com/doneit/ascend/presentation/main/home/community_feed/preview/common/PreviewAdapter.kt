package com.doneit.ascend.presentation.main.home.community_feed.preview.common

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.presentation.common.binding_adapters.setImage
import com.doneit.ascend.presentation.main.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.pager_item_image.view.*
import kotlinx.android.synthetic.main.pager_item_video.view.*

open class PreviewAdapter(
    private val items: ArrayList<Attachment>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.contentType) {
            ContentType.IMAGE -> {
                instantiateImage(container, item)
            }
            ContentType.VIDEO -> {
                instantiateVideo(container, item)
            }
        }
    }

    private fun instantiateVideo(
        container: ViewGroup,
        item: Attachment
    ): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.pager_item_video, container, false)
            .also {
                val player = SimpleExoPlayer.Builder(container.context)
                    .build()
                it.pvPlayer.player = player
                player.playWhenReady = false
                player.prepare(createMediaSource(container.context, item.url))
                container.addView(it)
            }
    }

    private fun instantiateImage(
        container: ViewGroup,
        item: Attachment
    ): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.pager_item_image, container, false)
            .also {
                container.addView(it)
                setImage(
                    it.ivImage,
                    item.url,
                    null
                )
            }
    }

    protected open fun createMediaSource(context: Context, url: String): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.getString(R.string.app_name))
            )
        ).createMediaSource(Uri.parse(url))
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount() = items.size

}