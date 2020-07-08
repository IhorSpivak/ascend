package com.doneit.ascend.presentation.utils.extensions

import android.app.Activity
import android.app.Service
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard(view: View) {
    val inputManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    inputManager?.showSoftInput(view, 0)
}

fun Fragment.hideKeyboard() {
    hideKeyboardOnView(view)
}

fun View.hideKeyboard() {
    hideKeyboardOnView(this)
}

fun Activity.hideKeyboard() {
    hideKeyboardOnView(window.decorView.rootView)
}

fun View.showKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

private fun hideKeyboardOnView(view: View?) {
    view?.apply {
        val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}