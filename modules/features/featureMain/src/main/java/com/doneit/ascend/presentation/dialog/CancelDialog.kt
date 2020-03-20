package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.CancelDialogBinding

class CancelDialog {
    companion object {
        fun create(
            context: Context,
            call: ((String) -> Unit)
        ): AlertDialog {

            val binding: CancelDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cancel_dialog,
                null,
                false
            )
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null, false)

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(binding.root)
                .create()

            binding.apply {
                btnPositive.setOnClickListener {
                    call.invoke(tvEditText.text.toString())
                }
                btnNegative.setOnClickListener {
                    dialog.dismiss()
                }
                btnPositive.isEnabled = false
                tvEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val r = Regex("^[a-zA-Z\\s-_.]{1,120}\$")
                        val text = s.toString()

                        btnPositive.isEnabled = text.matches(r)
                    }
                })
            }

            return dialog
        }
    }
}