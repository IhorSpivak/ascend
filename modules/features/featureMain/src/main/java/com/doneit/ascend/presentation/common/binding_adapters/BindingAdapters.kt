package com.doneit.ascend.presentation.common.binding_adapters

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.main.home.daily.common.groups.GroupAdapter
import com.doneit.ascend.presentation.main.search.common.SearchAdapter
import com.doneit.ascend.presentation.models.group.GroupListWithUser
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter


@BindingAdapter("app:html")
fun TextView.setAdapter(source: String?) {
    if (source != null) {
        text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("app:cardNumber")
fun TextView.setCardNumber(source: String?) {
    if (source != null) {
        val result = StringBuilder("XXXX  XXXX  XXXX  XXXX")

        source.forEachIndexed { index, c ->
            result.replace(index, index + 1, c.toString())
        }

        text = result.toString()
    }
}

@BindingAdapter("app:error")
fun TextView.setText(text: LiveData<Int?>?) {
    if (text != null && text.value != null) {
        this.text = resources.getString(text.value!!)
    } else {
        this.text = ""
    }
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupHorListAdapter,
    groups: LiveData<GroupListWithUserPaged>
) {

    if (view.adapter is GroupHorListAdapter) {
        groups.value?.let {
            (view.adapter as GroupHorListAdapter).setUser(it.user)
            (view.adapter as GroupHorListAdapter).submitList(it.groups!!)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setImage", "app:placeholder", requireAll = false)
fun setImage(view: AppCompatImageView, url: String?, placeholder: Drawable?) {
    Glide.with(view)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(placeholder)
        .into(view)
}

@BindingAdapter("app:setImage", "app:placeholder", requireAll = false)
fun setImage(view: AppCompatImageView, uri: Uri?, placeholder: Drawable?) {
    Glide.with(view)
        .load(uri)
        .placeholder(placeholder)
        .into(view)
}

@BindingAdapter("app:setCircleImage", "app:circlePlaceholder", requireAll = false)
fun setCircleImage(view: AppCompatImageView, url: String?, placeholder: Drawable?) {
    Glide.with(view)
        .load(url)
        .circleCrop()
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(view)
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupAdapter,
    groups: LiveData<GroupListWithUser>
) {

    if (view.adapter is GroupAdapter) {
        groups.value?.let {
            (view.adapter as GroupAdapter).setUser(it.user)
            (view.adapter as GroupAdapter).submitList(it.groups)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setVisibility")
fun setPlaceholderVisibility(
    view: androidx.constraintlayout.widget.ConstraintLayout,
    groups: LiveData<GroupListWithUser>
) {
    view.visibility =
        if (groups.value == null || groups.value?.groups?.isEmpty() == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:setVisibility")
fun setPlaceholderVisibility(
    view: androidx.recyclerview.widget.RecyclerView,
    groups: LiveData<GroupListWithUser>
) {
    view.visibility =
        if (groups.value == null || groups.value?.groups?.isEmpty() == true) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:setVisibility")
fun setVisibility(view: Button, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibility")
fun View.setVisibility(isShow: Boolean) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

@BindingAdapter("app:layout_constraintHorizontal_bias")
fun View.setConstrainHorizontalBias(value: Float) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    params.horizontalBias = value
    layoutParams = params
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: SearchAdapter,
    entities: LiveData<PagedList<SearchEntity>>?
) {

    if (view.adapter is SearchAdapter) {
        entities?.value?.let {
            (view.adapter as SearchAdapter).submitList(it)

            return
        }
    }

    view.adapter = adapter
}

@BindingAdapter("app:setAdapter", "app:items")
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: ParticipantAdapter,
    items: LiveData<List<String>>
) {
    if (view.adapter is ParticipantAdapter) {
        items.value?.let {
            (view.adapter as ParticipantAdapter).update(it)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: AttachmentsAdapter,
    entities: LiveData<PagedList<AttachmentEntity>>?
) {

    if (view.adapter is SearchAdapter) {
        entities?.value?.let {
            (view.adapter as AttachmentsAdapter).submitList(it)

            return
        }
    }

    view.adapter = adapter
}

@BindingAdapter("app:path")
fun AppCompatImageView.setImageUri(path: String?) {
    setImageURI(null)//in order to force image update
    path?.let {
        setImageURI(Uri.parse(it))
    }
}

@BindingAdapter("app:setAdapter")
fun setAdapter(
    view: androidx.viewpager.widget.ViewPager,
    adapter: com.doneit.ascend.presentation.main.master_mind.common.TabAdapter
) {
    view.adapter = adapter
}

@BindingAdapter("app:visibilityByData")
fun setVisibilityByData(
    view: androidx.appcompat.widget.AppCompatTextView,
    data: String?
) {
    view.visibility = if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("app:setCountBackground")
fun RadioButton.setCountBackground(groupType: GroupType) {
    background = when (groupType) {
        GroupType.MASTER_MIND -> resources.getDrawable(R.drawable.day_button_selector)
        GroupType.INDIVIDUAL -> resources.getDrawable(R.drawable.day_button_selector)
        GroupType.WEBINAR -> resources.getDrawable(R.drawable.number_button_selector_webinar)
        GroupType.SUPPORT -> resources.getDrawable(R.drawable.day_button_selector_support)
        else -> resources.getDrawable(R.drawable.day_button_selector)
    }
}

@BindingAdapter("app:setDayBackground")
fun CompoundButton.setDayBackground(groupType: GroupType) {
    background = when (groupType) {
        GroupType.MASTER_MIND -> resources.getDrawable(R.drawable.day_button_selector)
        GroupType.INDIVIDUAL -> resources.getDrawable(R.drawable.day_button_selector)
        GroupType.WEBINAR -> resources.getDrawable(R.drawable.day_button_selector_webinar)
        GroupType.SUPPORT -> resources.getDrawable(R.drawable.day_button_selector_support)
        else -> resources.getDrawable(R.drawable.day_button_selector)
    }
}

@BindingAdapter("app:setOkBackground")
fun Button.setOkBackground(groupType: GroupType) {
    background = when (groupType) {
        GroupType.MASTER_MIND -> resources.getDrawable(R.drawable.button_ok_selector)
        GroupType.INDIVIDUAL -> resources.getDrawable(R.drawable.button_ok_selector)
        GroupType.WEBINAR -> resources.getDrawable(R.drawable.button_ok_selector_webinar)
        GroupType.SUPPORT -> resources.getDrawable(R.drawable.button_ok_selector_support)
        else -> resources.getDrawable(R.drawable.button_ok_selector)
    }
}

@BindingAdapter("iconTint")
fun AppCompatButton.setIconTint(color: Int) {
    for (drawable in compoundDrawablesRelative) {
        if (drawable != null) {
            drawable.mutate()
            drawable.colorFilter = PorterDuffColorFilter(
                color,
                PorterDuff.Mode.SRC_IN
            )
        }
    }
}
