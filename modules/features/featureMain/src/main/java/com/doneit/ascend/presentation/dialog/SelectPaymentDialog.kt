package com.doneit.ascend.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.doneit.ascend.presentation.common.FullDividerItemDecorator
import com.doneit.ascend.presentation.dialog.common.CardsAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.models.PresentationCardModel
import kotlinx.android.synthetic.main.dialog_select_card.view.*

class SelectPaymentDialog {
    companion object {
        fun create(
            context: Context,
            adapter: CardsAdapter,
            onItemSelected: (PresentationCardModel) -> Unit
        ): AlertDialog {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_select_card, null, false)

            val dialog = AlertDialog.Builder(context, R.style.AppThemeAlertDialog)
                .setCancelable(false)
                .setView(mDialogView)
                .create()

            val decorator = FullDividerItemDecorator(mDialogView.context)
            mDialogView.rvCards.adapter = adapter
            mDialogView.rvCards.addItemDecoration(decorator)
            mDialogView.ivClose.setOnClickListener {
                dialog.dismiss()
            }
            mDialogView.btnNegative.setOnClickListener {
                dialog.dismiss()
            }
            mDialogView.btnPositive.setOnClickListener {
                onItemSelected.invoke(adapter.getSelectedItem()!!)
                dialog.dismiss()
            }
            adapter.hasSelectionListener = {
                mDialogView.btnPositive.isEnabled = it
            }

            return dialog
        }
    }
}