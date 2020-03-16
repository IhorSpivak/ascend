package com.doneit.ascend.presentation.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.view_disabled_text_view.view.*

@InverseBindingMethods(
    value = [
        InverseBindingMethod(
            type = EditWithError::class,
            attribute = "text",
            method = "getText"
        )
    ]
)

@BindingMethods(
    value = [
        BindingMethod(
            type = EditWithError::class,
            attribute = "text",
            method = "setText"

        ),
        BindingMethod(
            type = DisabledTextView::class,
            attribute = "hint",
            method = "setHint"
        ),
        BindingMethod(
            type = DisabledTextView::class,
            attribute = "src",
            method = "setSrc"

        ),
        BindingMethod(
            type = DisabledTextView::class,
            attribute = "tint",
            method = "setTint"
        )
    ]
)
class DisabledTextView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_disabled_text_view, this)
    }

    var text: String = ""
        set(value) {
            editText.hint = value
            field = value
        }
    fun setHint(hint: String?) {
        textLayout.hint = hint
    }

    fun setSrc(src: Drawable?) {
        icon.setImageDrawable(src)
        icon.visibility = if (src == null) View.GONE else View.VISIBLE
        requestLayout()
    }
    fun setTint(color: Int){
        icon.setColorFilter(color , PorterDuff.Mode.SRC_IN)
        requestLayout()
    }

    override fun getBaseline(): Int {
        return textLayout.baseline
    }
}