package com.doneit.ascend.presentation.main.create_group.create_support_group.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.doneit.ascend.presentation.main.R
import kotlinx.android.synthetic.main.view_spinner_title.view.*

class DurationAdapter(
    val types: Array<String>
) : BaseAdapter() {

    override fun getItemId(p0: Int) = 0L

    override fun getItem(position: Int): String? = types[position]

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_spinner_title, parent, false)

        view?.let {
            it.title.hint = view.resources.getString(R.string.meeting_format)
            it.title.text = types[position]
            if (position == 0) {
                it.title.setTextColor(view.resources.getColor(R.color.light_gray_b1bf))
            }
        }
        "".isNullOrBlank()
        return view
    }

    override fun getCount(): Int = types.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position == 0) {
            return convertView ?: initialSelection(parent!!)
        }
        val view =
            if (convertView != null && convertView is ConstraintLayout) {
                convertView
            } else {
                LayoutInflater
                    .from(parent?.context)
                    .inflate(R.layout.template_meeting_format, parent, false)
            }

        view?.let {
            if (it.tag != null) {
                (it.tag as FormatHolder).bind(types[position])
            } else {
                val holder = FormatHolder(view)
                holder.bind(types[position])
                it.tag = holder
            }
            return it
        }

        return view
    }

    private fun initialSelection(parent: ViewGroup): View {
        val view = TextView(parent.context)
        view.height = 0

        return view
    }
}