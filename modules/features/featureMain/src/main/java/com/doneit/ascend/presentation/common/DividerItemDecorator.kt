package com.doneit.ascend.presentation.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R

class DividerItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    var paddingRight = 0
    var paddingLeft = 0
    private val divider: Drawable =
        ContextCompat.getDrawable(context, R.drawable.recycler_item_decorator)!!

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
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
}