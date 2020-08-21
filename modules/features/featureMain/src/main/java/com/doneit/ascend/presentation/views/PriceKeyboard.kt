package com.doneit.ascend.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.custom_price_input.view.*

class PriceKeyboard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var editor: EditText? = null
    var onButtonsClick: OnButtonsClick? = null

    private val clickHandle = OnClickListener {
        if (it.id == R.id.key_backspace) {
            editor?.text.apply {
                this?.length.let { length ->
                    if (length!! > 0 && editor!!.selectionStart > 0) {
                        this?.delete(editor!!.selectionStart - 1, editor!!.selectionStart)
                    }
                }
            }
        } else {
            editor?.text?.apply {
                insert(editor!!.selectionStart, (it as TextView).text);
            }
        }
    }

    init {
        inflate(context, R.layout.custom_price_input, this)
        key_0.setOnClickListener(clickHandle)
        key_1.setOnClickListener(clickHandle)
        key_2.setOnClickListener(clickHandle)
        key_3.setOnClickListener(clickHandle)
        key_4.setOnClickListener(clickHandle)
        key_5.setOnClickListener(clickHandle)
        key_6.setOnClickListener(clickHandle)
        key_7.setOnClickListener(clickHandle)
        key_8.setOnClickListener(clickHandle)
        key_9.setOnClickListener(clickHandle)
        key_dot.setOnClickListener(clickHandle)
        key_backspace.setOnClickListener(clickHandle)
        btnDone.setOnClickListener { onButtonsClick?.onDoneClick() }
        btnCancel.setOnClickListener { onButtonsClick?.onCancelClick() }
    }

    interface OnButtonsClick {
        fun onDoneClick()
        fun onCancelClick()
    }

    fun setBackground(color: Int) {
        buttons.setBackgroundResource(color)
        btnDone.setBackgroundResource(color)
        btnCancel.setBackgroundResource(color)
    }
}