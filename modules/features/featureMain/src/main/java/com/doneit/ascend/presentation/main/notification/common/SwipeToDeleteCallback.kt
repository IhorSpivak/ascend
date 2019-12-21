package com.doneit.ascend.presentation.main.notification.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import kotlin.math.roundToInt

class SwipeToDeleteCallback(
    private val context: Context,
    private val onRemoveListener: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var canRemove = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            onRemoveListener.invoke(position)
            canRemove = true
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val p = Paint()
        val icon: Bitmap

        if (dX < 0) {
            icon = drawableToBitmap(context.getDrawable(R.drawable.ic_delete)!!)!!

            val left = itemView.right.toFloat() - convertDpToPx(20) - icon.width
            val right = itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2

            c.drawBitmap(
                icon,
                left,
                right,
                p
            )

//            recyclerView.setOnTouchListener { _, event ->
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE &&
//                    event.action == MotionEvent.ACTION_DOWN &&
//                    event.x >= left && event.x <= right &&
//                    event.y <= icon.height && event.y >= 0
//                ) {
//                    Log.e("myLog", "Remove element")
//                }
//
//                false
//            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX / 8, dY, actionState, isCurrentlyActive)
    }

    private fun convertDpToPx(dp: Int): Int {
        return (dp * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}