package com.doneit.ascend.presentation.main.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var swipeEnabled = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return when (swipeEnabled) {
            true -> super.onTouchEvent(ev)
            false -> false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return when (swipeEnabled) {
            true -> super.onInterceptTouchEvent(ev)
            false -> false
        }
    }
}