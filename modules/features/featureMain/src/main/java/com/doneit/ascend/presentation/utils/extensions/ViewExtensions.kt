package com.doneit.ascend.presentation.utils.extensions

import android.view.View

fun View.visible(isVisible: Boolean = true) {
    visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
}