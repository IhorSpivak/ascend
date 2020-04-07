package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogImageChooserBinding
import com.doneit.ascend.presentation.main.databinding.DialogPatternBinding

class ChooseImageDialog {
    companion object {
        fun create(
            context: Context,
            camera: (() -> Unit),
            gallery: (() -> Unit)
        ): AlertDialog {

            val binding: DialogImageChooserBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_image_chooser,
                null,
                false
            )
            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(binding.root)
                .create()

            binding.apply {
                tvCamera.setOnClickListener {
                    dialog.dismiss()
                    camera.invoke()
                }
                tvGallery.setOnClickListener {
                    dialog.dismiss()
                    gallery.invoke()
                }
                tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
            }
            return dialog
        }
    }
}