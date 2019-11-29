package com.doneit.ascend.presentation.login.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.*
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.R
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

    private var isCheckOnMaxLength = false
    private var maxLength = 0

    init {
        View.inflate(context, R.layout.view_edit_with_error, this)
    }

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

    private var listener: InverseBindingListener? = null

    fun setListener(listener: InverseBindingListener) {
        this.listener = listener
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText.removeTextChangedListener(this)

                val builder = StringBuilder()

                val currentText = if (isCheckOnMaxLength && s.toString().length >= maxLength)
                    s.toString().substring(0, maxLength)
                else
                    s.toString()

                val lastSymbol =
                    if (currentText.isNotEmpty()) currentText[currentText.length - 1] else ' '
                val currSelection = editText.selectionEnd

                if (everWordWithCapitalLetter) {

                    val words = currentText.split(" ")

                    for (value in words) {
                        if (value.isEmpty()) {
                            continue
                        }

                        builder.append(value.capitalize())
                        builder.append(" ")
                    }

                    var formattedTest = builder.toString()

                    if (lastSymbol != ' ') {
                        formattedTest = formattedTest.substring(0, formattedTest.length - 1)
                    }

                    if (formattedTest.startsWith(" ")) {
                        editText.setText(formattedTest.trim())
                    } else {
                        editText.setText(formattedTest)

                        if (currSelection + 1 == formattedTest.length) {
                            editText.setSelection(formattedTest.length)
                        } else {
                            editText.setSelection(currSelection)
                        }
                    }
                } else {
                    if (currentText.startsWith(" ")) {
                        editText.setText(currentText.trim())
                    }
                    else {
                        editText.setText(currentText)
                        editText.setSelection(if(currSelection > currentText.length) currentText.length else currSelection)
                    }
                }

                editText.addTextChangedListener(this)

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
        isCheckOnMaxLength = true
        maxLength = length
    }

    override fun getBaseline(): Int {
        return textLayout.baseline
    }
}
