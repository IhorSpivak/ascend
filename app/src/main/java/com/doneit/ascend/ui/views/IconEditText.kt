package com.doneit.ascend.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import com.doneit.ascend.R
import kotlinx.android.synthetic.main.icon_edit_text.view.*

@BindingAdapter("app:icon")
fun IconEditText.icon(drawable: Drawable?) {
    if (drawable == null) {
        image.visibility = View.GONE
    } else {
        image.setImageDrawable(drawable)
    }
}

@BindingAdapter("app:hint")
fun IconEditText.hint(hint: String) {
    textLayout.hint = hint
}

@BindingAdapter("app:error")
fun IconEditText.error(error: String?) {
    textLayout.error = error
}

@BindingAdapter("app:text")
fun IconEditText.setText(text: String?) {
    editText.setText(text)
}

@InverseBindingMethods(
    value = [
        InverseBindingMethod(
            type = IconEditText::class,
            attribute = "text",
            method = "getText"
        )
    ]
)
@BindingMethods(
    value = [
        BindingMethod(
            type = IconEditText::class,
            attribute = "textAttrChanged",
            method = "setText"

        )
    ]
)
class IconEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL

        View.inflate(context, R.layout.icon_edit_text, this)
    }

    fun getText() = editText.text.toString()

    private var listener: InverseBindingListener? = null

    fun setText(listener: InverseBindingListener) {
        this.listener = listener
        editText.doOnTextChanged { text, start, count, after ->
            listener.onChange()
        }
    }
}
