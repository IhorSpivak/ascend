package com.doneit.ascend.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogChatParticipantActionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChatParticipantActions {
    companion object {
        fun create(
            context: Context,
            user: SocketEventEntity
        ): Dialog {
            val binding: DialogChatParticipantActionsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_chat_participant_actions,
                null,
                false
            )
            binding.item = user

            val dialog = BottomSheetDialog(context, R.style.AppThemeAlertDialog)
            dialog.setContentView(binding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

           binding.tvCancel.setOnClickListener {
               dialog.dismiss()
           }

            return dialog
        }
    }
}