package com.doneit.ascend.presentation.views

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.widget.VideoView

open class LivestreamVideoView : VideoView {
    private var mForceHeight = 0
    private var mForceWidth = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    fun setDimensions(orientation: Int, width: Int, height: Int) {
        mForceWidth = if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height * height / width
        } else {
            width
        }
        mForceHeight = height
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mForceWidth, mForceHeight);
    }
}