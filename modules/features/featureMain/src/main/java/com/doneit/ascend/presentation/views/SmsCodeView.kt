package com.doneit.ascend.presentation.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import kotlinx.android.synthetic.main.view_sms_code.view.*

@InverseBindingMethods(
    value = [
        InverseBindingMethod(
            type = SmsCodeView::class,
            attribute = "text",
            method = "getText"
        )
    ]
)
@BindingMethods(
    value = [
        BindingMethod(
            type = SmsCodeView::class,
            attribute = "textAttrChanged",
            method = "setBindingListener"

        ),
        BindingMethod(
            type = SmsCodeView::class,
            attribute = "text",
            method = "setText"
        )
    ]
)
class SmsCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnKeyListener,
    TextView.OnEditorActionListener {

    private var mSubmitListener: OnSubmitListener? = null
    private lateinit var digits: List<EditText>

    var text: String
        get() {
            val builder = StringBuilder()
            digits.forEach {
                builder.append(it.text.toString())
            }
            return builder.toString()
        }
        set(value) {
            if(value != text) {
                digits.forEachIndexed { index, editText ->
                    if(value.length < index) {
                        editText.setText(value[index].toString())
                    } else {
                        editText.setText("")
                    }
                }
            }
        }

    init {
        View.inflate(context, R.layout.view_sms_code, this)
    }

    private var listener: InverseBindingListener? = null

    fun setBindingListener(listener: InverseBindingListener) {
        this.listener = listener
    }

    fun setSubmitListener(submitListener: OnSubmitListener) {
        this.mSubmitListener = submitListener
    }

    fun requestFirstFocus() {
        digits[0].showKeyboard()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        digits = listOf(etDigit1, etDigit2, etDigit3, etDigit4)

        initTextWatchers()
        initKeyListener()

        etDigit4!!.setOnEditorActionListener(this)
    }

    private fun initTextWatchers() {
        digits.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                if (text?.isEmpty() != true && index + 1 < digits.size) {
                    digits[index + 1].requestFocus()
                }
                listener?.onChange()
            }
        }
    }

    private fun initKeyListener() {
        digits.forEach {
            it.setOnKeyListener(this)
        }
    }

    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyEvent.action == KeyEvent.ACTION_UP) return false

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            when (view.id) {
                R.id.etDigit1 -> etDigit1.setText("")
                R.id.etDigit2 -> if (TextUtils.isEmpty(etDigit2.text)) {
                    etDigit1.setText("")
                    etDigit1.requestFocus()
                } else {
                    etDigit2.setText("")
                }
                R.id.etDigit3 -> if (TextUtils.isEmpty(etDigit3.text)) {
                    etDigit2.setText("")
                    etDigit2.requestFocus()
                } else {
                    etDigit3.setText("")
                }
                R.id.etDigit4 -> if (TextUtils.isEmpty(etDigit4.text)) {
                    etDigit3.setText("")
                    etDigit3.requestFocus()
                } else {
                    etDigit4.setText("")
                }
            }
            return true
        }
        return false
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId and EditorInfo.IME_MASK_ACTION != 0) {
            mSubmitListener?.onSubmit()
            return true
        } else {
            return false
        }
    }

    interface OnSubmitListener {
        fun onSubmit()
    }

    companion object {
        const val NUMBERS_COUNT = 4
    }
}