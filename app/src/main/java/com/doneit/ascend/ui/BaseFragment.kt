package com.doneit.ascend.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment {

    constructor(): super()
    constructor(@LayoutRes layoutID: Int) : super(layoutID)
}
