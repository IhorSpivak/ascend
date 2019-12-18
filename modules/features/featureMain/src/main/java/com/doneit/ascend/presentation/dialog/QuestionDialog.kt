package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogQuestionBinding

class QuestionDialog {

    companion object {
        fun create(
            context: Context,
            title: String,
            posBtnRes: Int,
            negBtnRes: Int,
            call: ((QuestionButtonType) -> Unit)
        ): AlertDialog {

            val binding: DialogQuestionBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_question,
                null,
                false
            )

            with(binding) {
                this.header = title
                this.buttonPositiveText = context.getString(posBtnRes)
                this.buttonNegativeText = context.getString(negBtnRes)
            }

            binding.btnPositive.setOnClickListener {
                call.invoke(QuestionButtonType.POSITIVE)
            }

            binding.btnNegative.setOnClickListener {
                call.invoke(QuestionButtonType.NEGATIVE)
            }

            binding.btnClose.setOnClickListener {
                call.invoke(QuestionButtonType.NEGATIVE)
            }

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(binding.root)

            return dialog.show()
        }
    }
}