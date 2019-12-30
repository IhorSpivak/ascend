package com.doneit.ascend.presentation.dialog

open class EditFieldDialogOptions(
    val titleRes: Int,
    val errorRes: Int,
    val initValue: String,
    val call: ((String) -> Unit)
)