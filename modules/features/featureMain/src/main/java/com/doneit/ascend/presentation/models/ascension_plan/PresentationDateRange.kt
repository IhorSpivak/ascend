package com.doneit.ascend.presentation.models.ascension_plan

import androidx.annotation.StringRes
import com.doneit.ascend.presentation.main.R

enum class PresentationDateRange(
    @StringRes val title: Int
) {
    TODAY(R.string.today),
    WEEKLY(R.string.weekly_tasks),
    MONTHLY(R.string.monthly_tasks),
}