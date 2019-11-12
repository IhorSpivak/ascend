package com.doneit.ascend.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.doneit.ascend.R
import kotlinx.android.synthetic.main.icon_edit_text.view.*

@BindingAdapter("app:icon")
fun IconEditText.icon(drawable: Drawable?) {
    if (drawable == null){
        image.visibility = View.GONE
    } else {
        image.setImageDrawable(drawable)
    }
}

@BindingAdapter("app:hint")
fun IconEditText.hint(hint: String) {
    textLayout.hint = hint
}

@BindingAdapter("app:text")
fun IconEditText.text(text: String?) {
    editText.setText(text)
}

//@InverseBindingAdapter(attribute = "app:text", event = "textAttrChanged")
//fun IconEditText.getText(text: String?) = editText.text.toString()

//@InverseBindingMethods(
//    value = [
//        InverseBindingMethod(
//            type = IconEditText::class,
//            attribute = "app:text",
//            method = "getText"
//        )
//    ]
//)
class IconEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL

        View.inflate(context, R.layout.icon_edit_text, this)
    }


}
