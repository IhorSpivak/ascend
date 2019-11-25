package com.doneit.ascend.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogConnectionBinding
import com.doneit.ascend.presentation.main.databinding.DialogErrorBinding
import com.doneit.ascend.presentation.main.databinding.DialogInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.showErrorDialog(errorTitle: String, errorMessage: String, buttonText: String) {

    val bindingAdapter = DialogErrorBinding.inflate(LayoutInflater.from(this.context), null, false)
    bindingAdapter.title = errorTitle
    bindingAdapter.description = errorMessage

    if (buttonText.isEmpty()) {
        bindingAdapter.btnAction.visibility = View.GONE
    } else {
        bindingAdapter.buttonText = buttonText
    }

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(bindingAdapter.root)

    if (buttonText.isNotEmpty()) {
        dialog.setCancelable(false)
    }

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    bindingAdapter.btnAction.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Fragment.showInfoDialog(errorTitle: String, errorMessage: String) : BottomSheetDialog {

    val bindingAdapter = DialogInfoBinding.inflate(LayoutInflater.from(this.context), null, false)
    bindingAdapter.title = errorTitle
    bindingAdapter.description = errorMessage

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(bindingAdapter.root)
    dialog.setCancelable(false)

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.show()

    return dialog
}

fun Fragment.showNoConnectionDialog(text: String): BottomSheetDialog {

    val bindingAdapter =
        DialogConnectionBinding.inflate(LayoutInflater.from(this.context), null, false)
    bindingAdapter.text = text

    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(bindingAdapter.root)

    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.show()

    return dialog
}