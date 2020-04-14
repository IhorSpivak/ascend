package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogPatternBinding

class DialogPattern {
    companion object {
        fun create(
            context: Context,
            title: String,
            description: String,
            okText: String,
            cancelText: String,
            call: (() -> Unit)
        ): AlertDialog {

            val binding: DialogPatternBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_pattern,
                null,
                false
            )
            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(binding.root)
                .create()

            binding.apply {
                titleText = title
                descriptionText = description
                this.okText = okText
                this.cancelText = cancelText
                btnPositive.setOnClickListener {
                    call.invoke()
                }
                btnNegative.setOnClickListener {
                    dialog.dismiss()
                }
            }
            return dialog
        }
    }
}