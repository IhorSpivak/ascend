package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogBlockBinding

class BlockUserDialog {
    companion object {
        fun create(
            context: Context,
            title: String,
            description: String,
            okText: String,
            cancelText: String,
            call: (() -> Unit)
        ): AlertDialog {

            val binding: DialogBlockBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_block,
                null,
                false
            )
            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(true)
                .setView(binding.root)
                .create()

            binding.apply {
                titleText = title
                descriptionText = description
                this.okText = okText
                this.cancelText = cancelText
                btnPositive.setOnClickListener {
                    call.invoke()
                    dialog.dismiss()
                }
                btnNegative.setOnClickListener {
                    dialog.dismiss()
                }
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
            return dialog
        }
    }
}