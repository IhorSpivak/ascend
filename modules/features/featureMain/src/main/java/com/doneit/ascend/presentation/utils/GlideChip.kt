package com.doneit.ascend.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.doneit.ascend.presentation.utils.extensions.createPlaceholderDrawable
import com.google.android.material.chip.Chip


class GlideChip : Chip {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

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
            .into(object : CustomViewTarget<GlideChip, Drawable>(this) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    chipIcon = context.createPlaceholderDrawable(placeholder.orEmpty())
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    chipIcon = resource
                }
            })
        return this
    }
}