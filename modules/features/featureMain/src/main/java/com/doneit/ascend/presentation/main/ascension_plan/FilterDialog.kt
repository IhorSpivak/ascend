package com.doneit.ascend.presentation.main.ascension_plan

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.doneit.ascend.domain.entity.dto.DateRangeDTO
import com.doneit.ascend.domain.entity.dto.FilterDTO
import com.doneit.ascend.domain.entity.dto.StepsDTO
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class FilterDialog {
    companion object{
        fun create(
            context: Context,
            applyChangesClick: ((FilterDTO) -> Unit)
        ): BottomSheetDialog {
            val binding = DialogFilterBinding.inflate(LayoutInflater.from(context), null, false)


            val dialog = BottomSheetDialog(context, R.style.TransparentBottomSheetDialog)
            dialog.setContentView(binding.root)
            dialog.setCancelable(false)

            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            binding.btnApply.setOnClickListener{ applyChangesClick(FilterDTO(DateRangeDTO.TODAY, StepsDTO.STEPS)) }
            binding.btnCancel.setOnClickListener { dialog.dismiss() }
            return dialog
        }
    }
}