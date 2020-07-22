package com.doneit.ascend.presentation.views.attachment_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.doneit.ascend.presentation.main.R

class AttachmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val mediaSize = resources.getDimensionPixelSize(R.dimen.fab_custom_size)
    private val defMargin = resources.getDimensionPixelSize(R.dimen.default_margin)
    private val circlePaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    }
    private val mediaIcon = ResourcesCompat.getDrawable(
        resources,
        R.drawable.ic_baseline_play,
        null
    )!!

    var shouldDrawMediaIcon = false
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shouldDrawMediaIcon) {
            canvas.drawCircle(width / 2f, height / 2f, mediaSize / 2f, circlePaint)
            mediaIcon.setBounds(
                (width - mediaSize + defMargin) / 2,
                (height - mediaSize + defMargin) / 2,
                (width + mediaSize - defMargin) / 2,
                (height + mediaSize - defMargin) / 2
            )
            mediaIcon.draw(canvas)
        }
    }

}