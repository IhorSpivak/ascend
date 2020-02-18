package com.doneit.ascend.presentation.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService
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

fun Long.toMb() : Float {
    return this.toFloat() / 1024 / 1024
}

fun String.copyToClipboard(context: Context) {
    val clipboard = getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("", this)
    clipboard!!.setPrimaryClip(clip)
}