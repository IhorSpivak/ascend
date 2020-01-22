package com.doneit.ascend.presentation.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.doneit.ascend.presentation.utils.Constants

fun List<String>.toErrorMessage(): String {
    val res = StringBuilder()
    forEach {
        res.append(it)
        res.append(Constants.END_OF_LINE)
    }
    if (res.isNotEmpty()) {
        res.setLength(res.length - 1)
    }
    return res.toString()
}

fun Activity.sendEmail(recipient: String){
    val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", recipient, null
        )
    )
    if (emailIntent.resolveActivity(this.packageManager) != null) {
        this.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}