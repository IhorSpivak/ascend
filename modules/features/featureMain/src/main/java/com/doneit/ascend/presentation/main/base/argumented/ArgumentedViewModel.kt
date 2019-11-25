package com.doneit.ascend.presentation.main.base.argumented

import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments

interface ArgumentedViewModel<A : BaseArguments> : BaseViewModel {
    fun applyArguments(args: A)
}