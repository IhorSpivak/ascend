package com.doneit.ascend.presentation.login.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.*
import com.doneit.ascend.presentation.login.R
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import androidx.core.widget.doOnTextChanged

@BindingAdapter("app:hint")
fun EditWithError.hint(hint: String) {
    textLayout.hint = hint
}

@BindingAdapter("app:error")
fun EditWithError.error(error: String?) {
    tvError.text = error
}

@BindingAdapter("app:text")
fun EditWithError.setText(text: String?) {
    if(text != editText.text.toString()){
        editText.setText(text)
    }
}

@BindingAdapter("android:inputType")
fun EditWithError.setInput(inputType: Int){
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
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL

        View.inflate(context, R.layout.view_edit_with_error, this)
    }

    fun getText() = editText.text.toString()

    private var listener: InverseBindingListener? = null

    fun setListener(listener: InverseBindingListener) {
        this.listener = listener
        editText.doOnTextChanged { text, start, count, after ->
            listener.onChange()
        }
    }
}
