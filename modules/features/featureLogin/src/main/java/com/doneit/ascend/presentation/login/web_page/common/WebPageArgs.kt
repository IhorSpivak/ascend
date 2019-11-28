package com.doneit.ascend.presentation.login.web_page.common

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class WebPageArgs(
    val title: String,
    val pageType: String
) : BaseArguments()