package com.doneit.ascend.presentation.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.doneit.ascend.presentation.main.databinding.DialogConnectionBinding
import com.google.android.material.snackbar.BaseTransientBottomBar

internal fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }

        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    return fallback
}

class ConnectionSnackbar(
    parent: ViewGroup,
    content: DialogConnectionBinding
) : BaseTransientBottomBar<ConnectionSnackbar>(parent, content.root, callback) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
        duration = LENGTH_INDEFINITE
    }

    companion object {

        fun make(view: View, text: String): ConnectionSnackbar {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView =
                DialogConnectionBinding.inflate(LayoutInflater.from(view.context), parent, false)
            customView.text = text

            return ConnectionSnackbar(
                parent,
                customView
            )
        }

        val callback = object : com.google.android.material.snackbar.ContentViewCallback {
            override fun animateContentOut(delay: Int, duration: Int) {

            }

            override fun animateContentIn(delay: Int, duration: Int) {

            }
        }
    }
}