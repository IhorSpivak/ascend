package com.doneit.ascend.presentation.views

import android.content.Context
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
import kotlinx.android.synthetic.main.view_edit_with_error.view.*

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
            type = EditWithError::class,
            attribute = "textAttrChanged",
            method = "setListener"

        ),
        BindingMethod(
            type = EditWithError::class,
            attribute = "hint",
            method = "setHint"

        ),
        BindingMethod(
            type = EditWithError::class,
            attribute = "error",
            method = "setError"

        ),
        BindingMethod(
            type = EditWithError::class,
            attribute = "src",
            method = "setSrc"

        ),
        BindingMethod(
            type = EditWithError::class,
            attribute = "inputType",
            method = "setInput"

        )
    ]
)
class EditWithError @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_edit_with_error, this)

        this.editText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(48)
        )
    }

    var lastText: String = ""

    var text: String
        get() {
            return editText.text.toString()
        }
        set(value) {
            if (value != editText.text.toString()) {
                editText.setText(value)
            }
        }

    var everWordWithCapitalLetter: Boolean = false
    var editing: Boolean = false

    private var listener: InverseBindingListener? = null

    fun setListener(listener: InverseBindingListener) {
        this.listener = listener

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val currentText = s.toString()

                if (everWordWithCapitalLetter &&
                    currentText != lastText
                ) {
                    if (currentText.length > lastText.length) {
                        val lastSymbol =
                            if (currentText.isNotEmpty()) currentText[currentText.length - 1] else null

                        val words = mutableListOf<String>()

                        for (value in currentText.split(" ")) {
                            if (value.isEmpty()) {
                                continue
                            }

                            words.add(value.capitalize())
                        }

                        var formattedTest = words.joinToString(separator = " ")

                        if (lastSymbol == ' ') {
                            formattedTest += lastSymbol
                        }

                        lastText = formattedTest

                        editText.setText(formattedTest)
                        editText.setSelection(formattedTest.length)
                    } else {
                        lastText = currentText
                    }
                }

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

    fun setInput(inputType: Int) {
        editText.inputType = inputType or InputType.TYPE_CLASS_TEXT
    }

    fun setDigits(values: String) {
        editText.keyListener = DigitsKeyListener.getInstance(values)
    }

    fun setSaveState(isSaveEnabled: Boolean) {
        editText.isSaveEnabled = isSaveEnabled
    }

    fun setMaxLength(length: Int) {
        this.editText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(length)
        )
    }

    fun setNoFocusable(isEnable: Boolean) {
        if (isEnable) {
            editText.inputType = InputType.TYPE_NULL
            editText.isFocusable = false
        }
    }

    override fun getBaseline(): Int {
        return textLayout.baseline
    }
}