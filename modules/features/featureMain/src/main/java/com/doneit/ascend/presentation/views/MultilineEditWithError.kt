package com.doneit.ascend.presentation.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.*
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.extensions.focusRequest
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.icon
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.textLayout
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.tvError

@InverseBindingMethods(
    value = [
        InverseBindingMethod(
            type = MultilineEditWithError::class,
            attribute = "text",
            method = "getText"
        )
    ]
)
@BindingMethods(
    value = [
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "text",
            method = "setText"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "textAttrChanged",
            method = "setListener"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "hint",
            method = "setHint"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "error",
            method = "setError"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "src",
            method = "setSrc"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "multilineInput",
            method = "setInput"

        ),
        BindingMethod(
            type = MultilineEditWithError::class,
            attribute = "digits",
            method = "setDigits"

        ),
        BindingMethod(
            type = EditWithError::class,
            attribute = "tint",
            method = "setTint"

        )
    ]
)
class MultilineEditWithError @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_multiline_edit_with_error, this)

        this.multilineEditText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(48)
        )
    }

    var text: String
        get() {
            return multilineEditText.text.toString()
        }
        set(value) {
            if (value != multilineEditText.text.toString()) {
                multilineEditText.setText(value)
                multilineEditText.setSelection(value.length)
            }
        }

    private var listener: InverseBindingListener? = null

    fun setListener(listener: InverseBindingListener) {
        this.listener = listener

        multilineEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                multilineEditText.removeTextChangedListener(this)

                val currentText = s.toString()
                if (currentText.startsWith(" ")) {
                    multilineEditText.setText(currentText.substring(1))
                }

                multilineEditText.addTextChangedListener(this)

                listener.onChange()
            }
        })
    }

    fun setHint(hint: String?) {
        textLayout.hint = hint
    }

    fun setError(error: LiveData<Int?>?) {
        if (error != null && error.value != null) {
            tvError.text = resources.getString(error.value!!)
        } else {
            tvError.text = ""
        }
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

    fun setDigits(values: String) {
        multilineEditText.keyListener = DigitsKeyListener.getInstance(values)
    }

    fun setMultilineInput(inputType: Int) {
        multilineEditText.inputType = inputType or InputType.TYPE_CLASS_TEXT
    }

    fun setSaveState(isSaveEnabled: Boolean) {
        multilineEditText.isSaveEnabled = isSaveEnabled
    }

    fun setNoFocusable(isEnable: Boolean) {
        if (isEnable) {
            multilineEditText.isFocusable = false
        }
    }

    fun setMaxLength(length: Int) {
        multilineEditText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(length)
        )
    }

    override fun getBaseline(): Int {
        return textLayout.baseline
    }
}