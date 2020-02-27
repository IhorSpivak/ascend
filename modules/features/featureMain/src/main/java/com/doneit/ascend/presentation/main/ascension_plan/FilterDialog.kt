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
    companion object {
        fun create(
            context: Context,
            filter: FilterDTO,
            applyChangesClick: ((FilterDTO) -> Unit)
        ): BottomSheetDialog {
            val binding = DialogFilterBinding.inflate(LayoutInflater.from(context), null, false)
            binding.filter = filter

            val dialog = BottomSheetDialog(context, R.style.TransparentBottomSheetDialog)
            dialog.setContentView(binding.root)
            dialog.setCancelable(false)

            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            binding.rgDateRange.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.radioDate0 -> filter.dateRange = DateRangeDTO.TODAY
                    R.id.radioDate1 -> filter.dateRange = DateRangeDTO.WEEKLY
                    R.id.radioDate2 -> filter.dateRange = DateRangeDTO.MONTHLY
                }
            }
            binding.rgSteps.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.radioStep0 -> filter.steps = StepsDTO.ALL
                    R.id.radioStep1 -> filter.steps = StepsDTO.SIRITUAL_STEPS
                    R.id.radioStep2 -> filter.steps = StepsDTO.GOALS
                }
            }
            binding.btnApply.setOnClickListener {
                applyChangesClick(filter)
                dialog.dismiss()
            }
            binding.btnCancel.setOnClickListener { dialog.dismiss() }
            return dialog
        }
    }
}