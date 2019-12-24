package com.doneit.ascend.presentation.main.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
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
        if (name.isNullOrBlank().not()) {
            val splited = name!!.split(' ')
            var res = name[0].toString()
            if (splited.size > 1) {
                res += splited[1][0]
            }
            tvPlaceholder.text = res
        }
    }

    fun setLongText(text: String?) {
        if (text.isNullOrBlank().not()) {
            tvLongPlaceholder.text = text
//            tvPlaceholder.text = text
//            tvPlaceholder.setTextColor(ContextCompat.getColor(context, android.R.color.white))
//            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvPlaceholder, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE)
//            tvPlaceholder.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18F)
        }
    }

    fun setUrl(url: String?) {
        Glide.with(ivIcon)
            .load(url)
            .into(ivIcon)
    }

    fun setRadius(radius: Float) {
        cvImage.radius = radius
    }
}