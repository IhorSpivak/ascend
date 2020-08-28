package com.doneit.ascend.presentation.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.TextOvalDrawable
import java.util.*

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

fun Activity.sendEmail(recipient: String) {
    val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", recipient, null
        )
    )
    if (emailIntent.resolveActivity(this.packageManager) != null) {
        this.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}

fun Activity.openLink(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}

fun Long.toMb(): Float {
    return this.toFloat() / 1024 / 1024
}

fun Fragment.shareTo(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Activity.shareTo(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun String.copyToClipboard(context: Context) {
    val clipboard = getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("", this)
    clipboard!!.setPrimaryClip(clip)
}

fun Context.createPlaceholderDrawable(title: String, overrideSize: Boolean = false): Drawable {
    val textOvalDrawable = TextOvalDrawable.Builder()
        .setText(title)
        .setTextColor(ContextCompat.getColor(this, R.color.light_gray_8f))
        .setColor(ContextCompat.getColor(this, R.color.light_gray_b1bf))
        .build()
    if (overrideSize) textOvalDrawable.overrideSize(256, 256)
    return textOvalDrawable
}

fun String.toCapitalLetter(): String {
    return this.substring(0, 1).toUpperCase() + this.substring(1).toLowerCase()
}

fun Int.minutesToMills(): Long {
    return this * 60 * 1000L
}

fun Date.toDayTime(): String {
    val formatter = "h:mm aa".toGMTFormatter()
    return formatter.format(this)
}