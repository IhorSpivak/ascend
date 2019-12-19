package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.dialog_report_abuse.view.*

class ReportAboseDialog {

    companion object {
        fun create(
            context: Context,
            call: ((String?) -> Unit)
        ): AlertDialog {

            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_report_abuse, null)

            mDialogView.btnPositive.setOnClickListener {
                call.invoke(mDialogView.tvReason.text.toString())
            }

            mDialogView.btnNegative.setOnClickListener {
                call.invoke(null)
            }

            mDialogView.btnClose.setOnClickListener {
                call.invoke(null)
            }

            mDialogView.tvReason.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val r = Regex("^[a-zA-Z\\s-_.]{1,120}\$")
                    val text = p0.toString()

                    if (text.isEmpty() || text.length > 120 || !text.matches(r)) {
                        // TODO: show error if needs

                    }
                }
            })

            mDialogView.tvReason.postDelayed(
                {
                    mDialogView.tvReason.clearFocus()
                }, 50
            )

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(mDialogView)

            return dialog.show()
        }
    }
}