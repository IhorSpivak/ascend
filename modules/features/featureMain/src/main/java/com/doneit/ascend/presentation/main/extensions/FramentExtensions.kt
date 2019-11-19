package com.doneit.ascend.presentation.main.extensions

import android.annotation.SuppressLint

@SuppressLint("CheckResult")
fun androidx.fragment.app.FragmentManager.replaceWithoutBackStack(containerId: Int,
                                                                  fragment: androidx.fragment.app.Fragment) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .addToBackStack(null)
        .commit()
}

@SuppressLint("CheckResult")
fun androidx.fragment.app.FragmentManager.replace(
    containerId: Int,
    fragment: androidx.fragment.app.Fragment
) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .commit()
}

@SuppressLint("CheckResult")
fun androidx.fragment.app.FragmentManager.addWithBackStack(
    containerId: Int,
    fragment: androidx.fragment.app.Fragment
) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .addToBackStack(fragment::class.java.simpleName)
        .commit()
}

@SuppressLint("CheckResult")
fun androidx.fragment.app.FragmentManager.replaceWithBackStack(
    containerId: Int,
    fragment: androidx.fragment.app.Fragment
) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .addToBackStack(fragment::class.java.simpleName)
        .commit()
}