package com.doneit.ascend.presentation.utils.extensions

import android.content.Context
import com.androidisland.ezpermission.EzPermission

fun Context.requestPermissions(
    permissions: List<String>,
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {}
) {
    EzPermission.with(this)
        .permissions(
            permissions
        )
        .request { granted, _, _ ->
            if (granted.containsAll(permissions)) {
                onGranted.invoke()
            } else {
                onDenied.invoke()
            }
        }
}