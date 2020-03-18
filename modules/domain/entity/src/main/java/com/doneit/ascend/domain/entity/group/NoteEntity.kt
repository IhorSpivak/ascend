package com.doneit.ascend.domain.entity.group

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NoteEntity(
    val content: String,
    val updatedAt: Date
): Parcelable