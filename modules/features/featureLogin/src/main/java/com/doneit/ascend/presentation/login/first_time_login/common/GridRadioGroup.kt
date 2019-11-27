package com.doneit.ascend.presentation.login.first_time_login.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.RadioButton

class GridRadioGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr), View.OnClickListener {

    override fun onClick(view: View) {

    }

    fun selectedFirst() {
        if (childCount > 0) {
            val radioButton = getChildAt(0) as RadioButton
            radioButton.isChecked = true
        }
    }

    fun clickByRadioButton(view: View) {
        for (index in 0 until childCount) {
            val radioButton = getChildAt(index) as RadioButton
            radioButton.isChecked = false
        }

        val radioButton = view as RadioButton
        radioButton.isChecked = true
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        child?.setOnClickListener(this)
    }
}