package com.doneit.ascend.presentation.main.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.view_master_mind_icon.view.*


@BindingMethods(
    value = [
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "name",
            method = "setName"

        ),
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "url",
            method = "setUrl"

        ),
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "radius",
            method = "setRadius"

        )
    ]
)
class MasterMindIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        View.inflate(context, R.layout.view_master_mind_icon, this)
    }

    fun setName(name: String?) {
        tvPlaceholder.text = name?.get(0)?.toString()
    }

    fun setUrl(url: String?) {
        Glide.with(ivIcon)
            .load(url)
            .into(ivIcon)
    }

    fun setRadius(radius: Float){
        cvImage.radius = radius
    }
}