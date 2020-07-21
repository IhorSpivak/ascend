package com.doneit.ascend.presentation.main.home.community_feed.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemFeedBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class PostViewHolder(
    itemView: View,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(post: Post) {
        with(binding) {
            postModel = post
            visibilityOfSend = true
            setupAttachments(post.attachments)
            applyResizing(post.attachments)
            setClickListeners(post)
        }
    }

    private fun ListItemFeedBinding.setClickListeners(
        post: Post
    ) {
        btnComments.setOnClickListener {
            postClickListeners.onCommentClick(post.id)
        }
        mmiAvatar.setOnClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        tvName.setOnClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        btnLike.setOnClickListener {
            postClickListeners.onLikeClick(post.isLikedMe, post.id, adapterPosition)
            post.isLikedMe = !post.isLikedMe
        }
        btnShare.setOnClickListener {
            postClickListeners.onShareClick(post.id)
        }
        btnBlock.setOnClickListener {
            if (post.isOwner) {
                postClickListeners.onOptionsClick(it, post)
            } else postClickListeners.onComplainClick(post.owner.id)
        }
        btnSend.setOnClickListener {
            if (etInputMessage.length() > 2) {
                postClickListeners.onSendCommentClick(
                    post.id,
                    etInputMessage.text?.toString().orEmpty(),
                    adapterPosition
                )
                etInputMessage.text?.clear()
                etInputMessage.hideKeyboard()
            }
        }
        vdFirst.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 0) }
        imvFirst.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 0) }
        vdSecond.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 1) }
        imvSecond.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 1) }
        imvThird.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 2) }
        imvFourth.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 3) }
        imvFifth.setOnClickListener { postClickListeners.onMediaClick(post.attachments, 4) }
    }

    private fun ListItemFeedBinding.setupAttachments(attachments: List<Attachment>) {
        if (attachments.isEmpty()) mivAttachments.gone()
        imvFirst.glideLoad(attachments.getOrNull(0))
        imvSecond.glideLoad(attachments.getOrNull(1))
        imvThird.glideLoad(attachments.getOrNull(2))
        imvFourth.glideLoad(attachments.getOrNull(3))
        imvFifth.glideLoad(attachments.getOrNull(4))
    }

    private fun ListItemFeedBinding.applyResizing(attachments: List<Attachment>) {
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

    private fun ListItemFeedBinding.calculateHeight(
        attachments: List<Attachment>,
        dividerWidth: Float = 1f,
        multiplierResult: Float = 1f,
        attachmentIndex: Int = 0
    ): Int {
        val width = if (mivAttachments.measuredWidth != 0) mivAttachments.measuredWidth
        else binding.root.measuredWidth - mivAttachments.marginStart - mivAttachments.marginEnd
        return (multiplierResult * attachments[attachmentIndex].size.aspectHeight(
            (width / dividerWidth).toInt()
        )).toInt()
    }

    private fun ListItemFeedBinding.resizeContainerByAttachments(
        attachments: List<Attachment>
    ) {

        fun updateParams() {
            mivAttachments.updateLayoutParams {
                height = when (attachments.size) {
                    1 -> calculateHeight(attachments)
                    2, 3 -> {
                        if (!attachments[0].isPortrait()) {
                            if (attachments[0].isEqual()) {
                                calculateHeight(attachments, dividerWidth = 3f)
                            } else {
                                calculateHeight(attachments, multiplierResult = 2f)
                            }
                        } else {
                            calculateHeight(attachments, dividerWidth = 2f)
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

    private fun applyPortraitLayoutFor2(set: ConstraintSet) {
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

    private fun applyLandscapeLayoutFor2(set: ConstraintSet) {
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

    private fun applyPortraitLayoutFor3(set: ConstraintSet) {
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

    private fun applyEqualLayoutFor3(set: ConstraintSet) {
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

    private fun applyLandscapeLayoutFor3(set: ConstraintSet) {
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

    private fun ListItemFeedBinding.applyLayoutFor4(
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

    private fun applyLayoutFor5(
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

    private fun ImageView.glideLoad(attachment: Attachment?) {
        visibleOrGone(attachment != null)
        attachment ?: return
        binding.mivAttachments.visible()
        when (attachment.contentType) {
            ContentType.IMAGE -> loadImage(attachment.url)
            ContentType.VIDEO -> loadImage(attachment.thumbnail)
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .error(R.drawable.ic_action_block)
            .placeholder(ColorDrawable(Color.LTGRAY))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(this)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            postClickListeners: PostClickListeners
        ): PostViewHolder {
            return PostViewHolder(
                DataBindingUtil.inflate<ListItemFeedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_feed,
                    parent,
                    false
                ).root,
                postClickListeners
            )
        }
    }
}