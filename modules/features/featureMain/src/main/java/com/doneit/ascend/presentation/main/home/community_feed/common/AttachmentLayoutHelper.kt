package com.doneit.ascend.presentation.main.home.community_feed.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.ViewPostContentBinding
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone
import com.doneit.ascend.presentation.views.attachment_view.AttachmentView

fun applyPortraitLayoutFor2(set: ConstraintSet) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        R.id.imvSecond,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
}

fun applyLandscapeLayoutFor2(set: ConstraintSet) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        R.id.imvSecond,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
}

fun applyPortraitLayoutFor3(set: ConstraintSet) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        R.id.imvSecond,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        R.id.imvThird,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.TOP,
        R.id.imvSecond,
        ConstraintSet.BOTTOM
    )
}

fun applyEqualLayoutFor3(set: ConstraintSet) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        R.id.imvSecond,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        R.id.imvThird,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.START,
        R.id.imvSecond,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
}

fun applyLandscapeLayoutFor3(set: ConstraintSet) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        R.id.imvSecond,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        R.id.imvThird,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.START,
        R.id.imvSecond,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
}

fun ViewPostContentBinding.applyLayoutFor4(
    attachments: List<Attachment>,
    set: ConstraintSet
) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        R.id.imvSecond,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        R.id.imvThird,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.END,
        R.id.imvFourth,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.START,
        R.id.imvSecond,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )

    set.connect(
        R.id.imvFourth,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.START,
        R.id.imvThird,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
    val weight = calculateHeight(attachments).toFloat() /
            (calculateHeight(attachments) + calculateHeight(
                attachments,
                dividerWidth = 3f
            ))
    set.setVerticalWeight(R.id.imvFirst, weight)
    set.setVerticalWeight(R.id.imvSecond, 1 - weight)
}

fun applyLayoutFor5(
    set: ConstraintSet
) {
    set.connect(
        R.id.imvFirst,
        ConstraintSet.END,
        R.id.imvThird,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.BOTTOM,
        R.id.imvSecond,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvFirst,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.TOP,
        R.id.imvFirst,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.END,
        R.id.imvThird,
        ConstraintSet.START
    )
    set.connect(
        R.id.imvSecond,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.BOTTOM,
        R.id.imvFourth,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvThird,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.BOTTOM,
        R.id.imvFifth,
        ConstraintSet.TOP
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFourth,
        ConstraintSet.TOP,
        R.id.imvThird,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvFifth,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    set.connect(
        R.id.imvFifth,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFifth,
        ConstraintSet.START,
        R.id.imvFirst,
        ConstraintSet.END
    )
    set.connect(
        R.id.imvFifth,
        ConstraintSet.TOP,
        R.id.imvFourth,
        ConstraintSet.BOTTOM
    )
}

fun ViewPostContentBinding.calculateHeight(
    attachments: List<Attachment>,
    dividerWidth: Float = 1f,
    multiplierResult: Float = 1f,
    attachmentIndex: Int = 0
): Int {
    val width = if (mivAttachments.measuredWidth != 0) mivAttachments.measuredWidth
    else root.measuredWidth - mivAttachments.marginStart - mivAttachments.marginEnd
    return (multiplierResult * attachments[attachmentIndex].size.aspectHeight(
        (width / dividerWidth).toInt()
    )).toInt()
}

fun AttachmentView.glideLoad(attachment: Attachment?) {
    visibleOrGone(attachment != null)
    attachment ?: return
    shouldDrawMediaIcon = attachment.contentType == ContentType.VIDEO
    when (attachment.contentType) {
        ContentType.IMAGE -> loadImage(attachment.url)
        ContentType.VIDEO -> loadImage(attachment.thumbnail)
    }
}

fun ViewPostContentBinding.resizeContainerByAttachments(
    attachments: List<Attachment>
) {
    mivAttachments.visibleOrGone(attachments.isNotEmpty())
    fun updateParams() {
        mivAttachments.updateLayoutParams {
            height = when (attachments.size) {
                1 -> calculateHeight(attachments)
                2, 3 -> when {
                    attachments[0].isPortrait() -> {
                        calculateHeight(attachments, dividerWidth = 2f)
                    }
                    attachments[0].isEqual() -> {
                        calculateHeight(attachments, dividerWidth = 3f)
                    }
                    else -> {
                        calculateHeight(attachments, multiplierResult = 2f)
                    }
                }
                4 -> calculateHeight(attachments) + calculateHeight(
                    attachments,
                    dividerWidth = 3f
                )
                5 -> calculateHeight(attachments, dividerWidth = 2f, multiplierResult = 2f)
                else -> 0
            }
            Log.d("SADAD", "HEIGHT: $height")
        }
    }
    if (mivAttachments.measuredHeight == 0) {
        mivAttachments.post {
            updateParams()
        }
        return
    }
    updateParams()
}

private fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_action_block)
        .placeholder(ColorDrawable(Color.LTGRAY))
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(ScaleToTarget())
        .into(this)
}

fun ViewPostContentBinding.setupAttachments(attachments: List<Attachment>) {
    if (attachments.isEmpty()) mivAttachments.gone()
    imvFirst.glideLoad(attachments.getOrNull(0))
    imvSecond.glideLoad(attachments.getOrNull(1))
    imvThird.glideLoad(attachments.getOrNull(2))
    imvFourth.glideLoad(attachments.getOrNull(3))
    imvFifth.glideLoad(attachments.getOrNull(4))
}

fun ViewPostContentBinding.applyResizing(attachments: List<Attachment>) {
    if (attachments.isEmpty()) return
    resizeContainerByAttachments(attachments)
    val set = ConstraintSet().apply { clone(mivAttachments) }
    val isPortraitMode = attachments[0].isPortrait()
    when (attachments.size) {
        1, 2 -> {
            if (!isPortraitMode) {
                applyLandscapeLayoutFor2(set)
            } else {
                applyPortraitLayoutFor2(set)
            }
        }
        3 -> {
            if (attachments[0].isEqual()) {
                applyEqualLayoutFor3(set)
            } else if (!isPortraitMode) {
                applyLandscapeLayoutFor3(set)
            } else {
                applyPortraitLayoutFor3(set)
            }
        }
        4 -> applyLayoutFor4(attachments, set)
        5 -> applyLayoutFor5(set)
    }
    set.applyTo(mivAttachments)
}