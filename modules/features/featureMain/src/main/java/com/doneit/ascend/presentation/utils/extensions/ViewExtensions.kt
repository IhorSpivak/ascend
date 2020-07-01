package com.doneit.ascend.presentation.utils.extensions

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.visible(isVisible: Boolean = true, isGone: Boolean = false) {
    visibility = if (isVisible) View.VISIBLE else if (isGone) View.GONE else View.INVISIBLE
}

fun View.visibleOrGone(isVisible: Boolean = true) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun EditText.focusRequest() {
    val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    requestFocus()
}

inline fun View.waitForLayout(crossinline f: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            f()
        }
    })
}