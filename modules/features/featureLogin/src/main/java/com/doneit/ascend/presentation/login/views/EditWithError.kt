package com.doneit.ascend.presentation.login.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.R
import kotlinx.android.synthetic.main.view_edit_with_error.view.*

@BindingAdapter("app:hint")
fun EditWithError.hint(hint: String?) {
    textLayout.hint = hint
}

@BindingAdapter("app:error")
fun EditWithError.error(error: String?) {
    tvError.text = error
}

@BindingAdapter("app:error")
fun EditWithError.error(error: LiveData<Int?>?) {
    if(error != null && error.value != null){
        tvError.text = resources.getString(error.value!!)
    } else {
        tvError.text = ""
    }
}

@BindingAdapter("app:text")
fun EditWithError.setText(text: String?) {
    if (text != editText.text.toString()) {
        editText.setText(text)
    }
}

@BindingAdapter("app:src")
fun EditWithError.setSrc(src: Drawable?) {
    icon.setImageDrawable(src)
    icon.visibility = if( src == null) View.GONE else View.VISIBLE
    requestLayout()
}

@BindingAdapter("app:inputType")
fun EditWithError.setInput(inputType: Int) {
    editText.inputType = inputType or InputType.TYPE_CLASS_TEXT
}

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
            attribute = "textAttrChanged",
            method = "setListener"

        )
    ]
)
class EditWithError @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_edit_with_error, this)
    }

    var text: String
        get() {
            return editText.text.toString()
        }
        set(value) {
            editText.setText(value)
        }

    private var listener: InverseBindingListener? = null

    fun setListener(listener: InverseBindingListener) {
        this.listener = listener
        editText.doOnTextChanged { text, start, count, after ->
            listener.onChange()
        }
    }

    override fun getBaseline(): Int {
        return editText.baseline
    }
}
