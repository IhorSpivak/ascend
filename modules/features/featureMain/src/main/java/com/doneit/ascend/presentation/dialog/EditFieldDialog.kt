package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.dialog_edit_field.view.*

class EditFieldDialog {

    companion object {
        fun create(
            context: Context,
            options: EditFieldDialogOptions
        ): AlertDialog {

            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_edit_field, null, false)

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(mDialogView)
                .create()

            mDialogView.btnPositive.setOnClickListener {
                options.call.invoke(mDialogView.tvEditText.text.toString())
                dialog.dismiss()
            }

            mDialogView.btnNegative.setOnClickListener {
                dialog.dismiss()
            }

            var lastText = ""
            mDialogView.tvEditText.hint = context.getString(options.hintRes)
            mDialogView.tvEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    val currentText = s.toString()

                    val r = Regex("[a-zA-Z ]{4,}")

                    if (currentText.matches(r).not()) {
                        mDialogView.btnPositive.isEnabled = false
                        mDialogView.tvError.visibility = View.VISIBLE
                    } else {
                        mDialogView.btnPositive.isEnabled = true
                        mDialogView.tvError.visibility = View.GONE
                    }

                    if (currentText != lastText) {

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

                            mDialogView.tvEditText.setText(formattedTest)
                            mDialogView.tvEditText.setSelection(formattedTest.length)
                        } else {
                            lastText = currentText
                        }
                    }
                }
            })

            mDialogView.title.setText(options.titleRes)
            mDialogView.tvEditText.setText(options.initValue)
            mDialogView.tvError.setText(options.errorRes)

            mDialogView.btnPositive.isEnabled = false
            lastText = options.initValue

            return dialog
        }
    }
}