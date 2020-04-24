package com.doneit.ascend.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.extensions.waitForLayout
import kotlinx.android.synthetic.main.view_master_mind_icon.view.*


@BindingMethods(
    value = [
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "fullName",
            method = "setName"

        ),
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "url",
            method = "setUrl"

        ),
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "urlCached",
            method = "setUrlCached"

        ),
        BindingMethod(
            type = MasterMindIcon::class,
            attribute = "placeholderRes",
            method = "setPlaceholderRes"

        )
    ]
)
class MasterMindIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_master_mind_icon, this)

        waitForLayout {
            cvImage.radius = height / 2.0f
        }
    }

    fun setName(name: String?) {
        if (name.isNullOrBlank().not()) {
            val splited = name!!.split(' ')
            var res = name[0].toString().capitalize()
            if (splited.size > 1) {
                res += splited[1][0]
            }
            tvPlaceholder.text = res
        }
    }

    fun setLongText(text: String?) {
        if (text.isNullOrBlank().not()) {
            tvLongPlaceholder.text = text?.capitalize()
        }
    }

    fun setUrl(url: String?) {

        ivIcon.setImageURI(null)
        ivIcon.setImageBitmap(null)
        ivIcon.setImageDrawable(null)

        Glide.with(ivIcon)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
            .into(ivIcon)
    }

    fun setUrlCached(url: String?) {
        Glide.with(ivIcon)
            .load(url)
            .into(ivIcon)
    }

    fun setPlaceholderRes(placeholder: Int) {
        Glide.with(ivIcon)
            .load(placeholder)
            .into(ivIcon)
    }
}