package com.doneit.ascend.presentation.common

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DividerItemDecorator(private val divider: Drawable) : RecyclerView.ItemDecoration() {

    var paddingRight = 0
    var paddingLeft = 0

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if(childCount > 0) {
            c.drawTop(parent.getChildAt(0))
            c.drawBottom(parent.getChildAt(0))
        }
        for (i in 1 until childCount) {
            c.drawBottom(parent.getChildAt(i))
        }
    }

    private fun Canvas.drawBottom(view: View) {
        val dividerLeft = paddingLeft
        val dividerRight = (view.parent as RecyclerView).width - paddingRight
        val params = view.layoutParams as RecyclerView.LayoutParams

        val dividerBottom = view.bottom + params.bottomMargin
        val dividerTop = dividerBottom - divider.intrinsicHeight

        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
        divider.draw(this)
    }

    private fun Canvas.drawTop(view: View) {
        val dividerLeft = paddingLeft
        val dividerRight = (view.parent as RecyclerView).width - paddingRight
        val params = view.layoutParams as RecyclerView.LayoutParams

        val dividerTop = view.top - params.topMargin
        val dividerBottom = dividerTop + divider.intrinsicHeight

        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
        divider.draw(this)
    }
}