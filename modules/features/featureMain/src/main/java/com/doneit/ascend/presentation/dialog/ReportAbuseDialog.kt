package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.dialog_report_abuse.view.*
import android.view.WindowManager



class ReportAbuseDialog {

    companion object {
        fun create(
            context: Context,
            call: ((String) -> Unit)
        ): AlertDialog {

            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_report_abuse, null, false)

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(mDialogView)
                .create()

            mDialogView.btnPositive.setOnClickListener {
                call.invoke(mDialogView.tvReason.text.toString())
            }

            mDialogView.btnNegative.setOnClickListener {
                dialog.dismiss()
            }

            mDialogView.btnClose.setOnClickListener {
                dialog.dismiss()
            }

            mDialogView.tvReason.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val r = Regex("^[a-zA-Z\\s-_.]{1,120}\$")
                    val text = p0.toString()

                    mDialogView.btnPositive.isEnabled = (text.isEmpty() || text.length > 120 || !text.matches(r)).not()
                }
            })

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            return dialog
        }
    }
}