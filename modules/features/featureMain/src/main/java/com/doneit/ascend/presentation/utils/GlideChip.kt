package com.doneit.ascend.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doneit.ascend.presentation.utils.extensions.createPlaceholderDrawable
import com.google.android.material.chip.Chip


class GlideChip : Chip {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    /**
     * Set an image from an URL for the [Chip] using [com.bumptech.glide.Glide]
     * @param url icon URL
     * @param errDrawable error icon if Glide return failed response
     */
    fun setIconUrl(
        url: String?,
        placeholder: String?
    ): GlideChip {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    chipIcon = this@GlideChip.context.createPlaceholderDrawable(placeholder!!)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    chipIcon = resource
                    return false
                }
            }).preload()
        return this
    }
}