package com.doneit.ascend.presentation.common

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.group_list.common.GroupListAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.doneit.ascend.presentation.main.home.group.common.GroupAdapter
import com.doneit.ascend.presentation.main.master_mind.list.common.MasterMindAdapter
import com.doneit.ascend.presentation.models.GroupListWithUser
import com.doneit.ascend.presentation.models.GroupListWithUserPaged
import com.doneit.ascend.presentation.main.search.common.SearchAdapter
import com.doneit.ascend.presentation.utils.DatePickerUtil
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:html")
fun TextView.setAdapter(source: String?) {
    if(source != null) {
        text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupListAdapter,
    groups: LiveData<GroupListWithUserPaged>
) {

    if (view.adapter is GroupListAdapter) {
        groups.value?.let {
            (view.adapter as GroupListAdapter).setUser(it.user)
            (view.adapter as GroupListAdapter).submitList(it.groups!!)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setImage", "app:placeholder", requireAll = false)
fun setImage(view: AppCompatImageView, url: String?, placeholder: Drawable?) {

    Glide.with(view)
        .load(url)
        .placeholder(placeholder)
        .into(view)
}

@BindingAdapter("app:setDate")
fun setDate(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date?) {

    try {

        dateTime?.let {
            val cal = Calendar.getInstance()
            cal.time = dateTime

            val month = cal[Calendar.MONTH]
            val dayOfMonth = cal[Calendar.DAY_OF_MONTH]

            val datePickerUtil = DatePickerUtil(view.context)

            view.text =
                String.format(
                    "%s %s",
                    dayOfMonth.toString(),
                    MonthEntity.values()[month].toString()
                )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:setTime")
fun setTime(view: androidx.appcompat.widget.AppCompatTextView, dateTime: Date) {
    try {

        dateTime?.let {
            val sdf = SimpleDateFormat("hh:mm aa (z)")
            view.text = sdf.format(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
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
            (view.adapter as GroupAdapter).submitList(it.groups!!)
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

@BindingAdapter("app:setParticipantsVisibility")
fun setParticipantsVisibility(
    view: androidx.recyclerview.widget.RecyclerView,
    items: LiveData<List<String>>
) {
    view.visibility = if (items.value != null && items.value?.isNotEmpty() == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:path")
fun AppCompatImageView.setImageUri(path: String?) {
    setImageURI(null)//in order to force image update
    path?.let {
        setImageURI(Uri.parse(it))
    }
}

@BindingAdapter("app:setAdapter")
fun setAdapter(view: androidx.viewpager.widget.ViewPager, adapter: com.doneit.ascend.presentation.main.master_mind.common.TabAdapter) {
    view.adapter = adapter
}

@BindingAdapter("app:visibilityByData")
fun setVisibilityByData(
    view: androidx.appcompat.widget.AppCompatTextView,
    data: String?
) {
    view.visibility = if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
}