package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogEditChatNameBinding
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.isValidChatTitle

class EditChatNameDialog {
    companion object {
        fun create(
            context: Context,
            chatTitle: String,
            call: ((String) -> Unit)
        ): AlertDialog {

            val binding: DialogEditChatNameBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_edit_chat_name,
                null,
                false
            )

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(binding.root)
                .create()

            val validatableField = ValidatableField().apply {
                validator = {s ->
                    val result = ValidationResult()
                    if (s.isValidChatTitle().not()) {
                        result.isSucceed = false
                        result.errors.add(R.string.error_group_name)
                    }

                    result
                }
                onFieldInvalidate = {
                    binding.btnPositive.isEnabled = this.isValid
                }
            }

            binding.apply {
                this.validatable = validatableField
                validatableField.observableField.set(chatTitle)
                btnPositive.setOnClickListener {
                    call.invoke(validatableField.observableField.get()!!)
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