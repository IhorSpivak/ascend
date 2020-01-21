package com.doneit.ascend.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogChatParticipantActionsBinding

class ChatParticipantActions {
    companion object {
        fun create(
            context: Context,
            user: SocketEventEntity,
            reportAbuse: (Long) -> Unit
        ): Dialog {
            val binding: DialogChatParticipantActionsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_chat_participant_actions,
                null,
                false
            )
            binding.item = user

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setView(binding.root)
                .setCancelable(false)
                .create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.gravity = Gravity.BOTTOM

            binding.tvCancel.setOnClickListener {
                dialog.dismiss()
            }
            binding.tvReport.setOnClickListener {
                reportAbuse.invoke(user.userId)
            }

            return dialog
        }
    }
}