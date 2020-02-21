package com.doneit.ascend.presentation.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.dialog_permissions_required.view.*


class PermissionsRequiredDialog {
    companion object {
        fun create(
            activity: Activity
        ): AlertDialog {
            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.dialog_permissions_required, null, false)

            val dialog = AlertDialog.Builder(activity, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(mDialogView)
                .create()

            mDialogView.tvCancel.setOnClickListener {
                dialog.dismiss()
            }

            mDialogView.tvSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivityForResult(intent, 0)
            }

            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return dialog
        }
    }
}