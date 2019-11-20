package com.doneit.ascend.presentation.login.views.phone_code

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

class CustomSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Spinner(context, attrs, defStyleAttr) {

    var clickListener: OnClickListener? = null

    override fun performClick(): Boolean {
        clickListener?.onClick(this)
        return super.performClick()
    }
}