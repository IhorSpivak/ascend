package com.doneit.ascend.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.DialogErrorBinding
import com.doneit.ascend.presentation.main.databinding.DialogInfoBinding
import com.doneit.ascend.presentation.views.ConnectionSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_change_photo.*
import kotlinx.android.synthetic.main.dialog_change_photo.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun BaseFragment<*>.showDefaultError(errorMessage: String) {
    showErrorDialog(
        getString(R.string.title_error),
        errorMessage,
        "",
        null
    )
}

fun BaseFragment<*>.showErrorDialog(
    errorTitle: String,
    errorMessage: String,
    buttonText: String,
    clickListener: IDialogClickListener? = null,
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
        binding.underline.visibility = View.GONE
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

    this.shownDialog?.dismiss()
    this.shownDialog = dialog
    dialog.show()
}

fun BaseFragment<*>.showInfoDialog(
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

    this.shownDialog?.dismiss()
    this.shownDialog = dialog
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

fun View.showNoConnectionDialog(
    text: String
): ConnectionSnackbar {

    val snackbar = ConnectionSnackbar.make(this, text)
    snackbar.show()

    return snackbar
}

fun Fragment.showChangePhotoDialog(
    takePhotoClick: (() -> Unit),
    rollCameraClick: (() -> Unit),
    deleteClick: () -> Unit,
    showDeleteButton: Boolean
) {
    val dialogView =
        LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_change_photo, null, false)
    val dialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    dialog.setContentView(dialogView)
    dialog.setCancelable(false)

    if (showDeleteButton.not()) {
        dialog.tvDeletePhoto.visibility = View.GONE
    }

    dialogView.tvTakePhoto.setOnClickListener {
        takePhotoClick.invoke()
        dialog.dismiss()
    }

    dialog.tvCameraRoll.setOnClickListener {
        rollCameraClick.invoke()
        dialog.dismiss()
    }

    dialog.tvDeletePhoto.setOnClickListener {
        deleteClick.invoke()
        dialog.dismiss()
    }

    dialog.tvCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}