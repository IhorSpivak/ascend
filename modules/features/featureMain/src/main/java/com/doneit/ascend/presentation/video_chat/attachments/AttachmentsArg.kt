package com.doneit.ascend.presentation.video_chat.attachments

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
open class AttachmentsArg(
    val groupId: Long,
    val isOwner: Boolean
) : BaseArguments()