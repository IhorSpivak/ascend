package com.doneit.ascend.presentation.utils

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.text.TextUtils

class TextOvalDrawable private constructor(builder: Builder) :
    ShapeDrawable(OvalShape()) {
    private val textPaint = Paint()

    private val text: String
    private var width: Int
    private var height: Int
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val r = bounds
        if (!TextUtils.isEmpty(text)) {
            val count = canvas.save()
            val width = if (width < 0) r.width() else width
            val height = if (height < 0) r.height() else height
            val fontSize = Math.min(width, height) / 2
            textPaint.textSize = fontSize.toFloat()
            canvas.drawText(
                text,
                width / 2.toFloat(),
                height / 2 - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint
            )
            canvas.restoreToCount(count)
        }
    }

    /*
        Fix for CircleImageView
     */
    fun overrideSize(width: Int, height: Int): TextOvalDrawable {
        this.width = width
        this.height = height
        return this
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return if (width < 1) super.getIntrinsicWidth() else width
    }

    override fun getIntrinsicHeight(): Int {
        return if (height < 1) super.getIntrinsicHeight() else height
    }

    class Builder {
        var text = ""
        var textColor = Color.WHITE
        var color = Color.BLACK
        var bold = false
        var uppercase = true
        var font = Typeface.create("sans-serif-light", Typeface.NORMAL)
        var height = -1
        var width = -1
        fun setText(text: String?): Builder {
            if (text.isNullOrBlank().not()) {
                val splited = text!!.trim().split(' ')
                var res = text[0].toString().capitalize()
                if (splited.size > 1) {
                    res += splited[1][0]
                }
                this.text = res
            }
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun setColor(color: Int): Builder {
            this.color = color
            return this
        }

        fun setBold(bold: Boolean): Builder {
            this.bold = bold
            return this
        }

        fun setUppercase(uppercase: Boolean): Builder {
            this.uppercase = uppercase
            return this
        }

        fun setFont(font: Typeface): Builder {
            this.font = font
            return this
        }

        fun build(): TextOvalDrawable {
            return TextOvalDrawable(this)
        }

        fun setSize(width: Int, height: Int): Builder {
            this.width = width
            this.height = height
            return this
        }
    }

    init {
        width = builder.width
        height = builder.height
        text = if (builder.uppercase) builder.text.toUpperCase() else builder.text
        textPaint.color = builder.textColor
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = builder.bold
        textPaint.style = Paint.Style.FILL
        textPaint.typeface = builder.font
        textPaint.textAlign = Paint.Align.CENTER
        paint.color = builder.color
    }
}
