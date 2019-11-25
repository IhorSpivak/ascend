package com.doneit.ascend.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogConnectionBinding
import com.doneit.ascend.presentation.main.databinding.DialogErrorBinding
import com.doneit.ascend.presentation.main.databinding.DialogInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.showDefaultError(errorMessage: String) {
    showErrorDialog(
        getString(R.string.title_error),
        errorMessage,
        "",
        null
    )
}

fun Fragment.showErrorDialog(
    errorTitle: String,
    errorMessage: String,
    buttonText: String,
    clickListener: IDialogClickListener?,
    isFullColored: Boolean = true,
    isAutoClose: Boolean = true
) {

    val binding = DialogErrorBinding.inflate(LayoutInflater.from(this.context), null, false)
    binding.title = errorTitle
    binding.description = errorMessage

    if (clickListener != null) {
        binding.clickListener = clickListener
    }

    if (buttonText.isEmpty()) {
        binding.btnAction.visibility = View.GONE
    } else {
        binding.buttonText = buttonText
    }

    if (isFullColored) {
        binding.dialogContainer.setBackgroundColor(
            ContextCompat.getColor(
                this.context!!,
                R.color.error_color
            )
        )
    }

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(binding.root)

    if (buttonText.isNotEmpty()) {
        dialog.setCancelable(false)
    }

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    binding.btnAction.setOnClickListener {
        dialog.dismiss()
    }

    if (isAutoClose) {
        GlobalScope.launch {
            delay(Constants.TIME_AUTO_CLOSE_DIALOG)
            dialog.dismiss()
        }
    }

    dialog.show()
}

fun Fragment.showInfoDialog(
    errorTitle: String,
    errorMessage: String,
    isAutoClose: Boolean = true
): BottomSheetDialog {

    val binding = DialogInfoBinding.inflate(LayoutInflater.from(this.context), null, false)
    binding.title = errorTitle
    binding.description = errorMessage

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.show()

    if (isAutoClose) {
        GlobalScope.launch {
            delay(Constants.TIME_AUTO_CLOSE_DIALOG)
            dialog.dismiss()
        }
    }

    return dialog
}

fun Fragment.showNoConnectionDialog(text: String, isAutoClose: Boolean = true): BottomSheetDialog {

    val binding =
        DialogConnectionBinding.inflate(LayoutInflater.from(this.context), null, false)
    binding.text = text

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(binding.root)

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.show()

    if (isAutoClose) {
        GlobalScope.launch {
            delay(Constants.TIME_AUTO_CLOSE_DIALOG)
            dialog.dismiss()
        }
    }

    return dialog
}