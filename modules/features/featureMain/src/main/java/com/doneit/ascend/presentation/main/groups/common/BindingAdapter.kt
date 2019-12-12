package com.doneit.ascend.presentation.main.groups.common

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.utils.DatePickerUtil
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:setDate")
fun setDate(view: androidx.appcompat.widget.AppCompatTextView, dateTime: String) {

    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date: Date? = dateFormat.parse(dateTime)

        date?.let {
            val cal = Calendar.getInstance()
            cal.time = date

            val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
            val dayOfMonth = cal[Calendar.DAY_OF_MONTH]

            val datePickerUtil = DatePickerUtil(view.context)

            view.text =
                String.format(
                    "%s %s",
                    dayOfMonth.toString(),
                    datePickerUtil.getStringValue(dayOfWeek)
                )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:setTime")
fun setTime(view: androidx.appcompat.widget.AppCompatTextView, dateTime: String) {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date: Date? = dateFormat.parse(dateTime)

        date?.let {

            val sdf = SimpleDateFormat("hh:mm aa")
            val formattedTime = sdf.format(it)

            val cal = Calendar.getInstance()
            cal.time = date

            val tz: TimeZone = cal.timeZone

            val isDayLight: Boolean = tz.inDaylightTime(date)
            view.text = String.format(
                "%s (%s)",
                formattedTime,
                tz.getDisplayName(isDayLight, TimeZone.SHORT)
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupAdapter,
    groups: LiveData<List<GroupEntity>>
) {

    if (view.adapter is GroupAdapter) {
        groups.value?.let {
            (view.adapter as GroupAdapter).updateData(it)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setVisibility")
fun setPlaceholderVisibility(
    view: androidx.constraintlayout.widget.ConstraintLayout,
    groups: LiveData<List<GroupEntity>>
) {
    view.visibility =
        if (groups.value == null || groups.value?.isEmpty() == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:setVisibility")
fun setPlaceholderVisibility(
    view: androidx.recyclerview.widget.RecyclerView,
    groups: LiveData<List<GroupEntity>>
) {
    view.visibility =
        if (groups.value == null || groups.value?.isEmpty() == true) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:setImage")
fun setImage(view: AppCompatImageView, url: String?) {

    Glide.with(view)
        .load(url)
        .into(view)
}