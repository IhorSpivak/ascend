package com.doneit.ascend.presentation.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.models.PresentationCommunityModel
import kotlinx.android.synthetic.main.item_community_spinner.view.*

class CommunityArrayAdapter(
    context: Context,
    private val spinner: Spinner,
    private val values: List<PresentationCommunityModel>
) : ArrayAdapter<PresentationCommunityModel>(context, 0, values) {

    override fun getCount() = values.size

    override fun getItem(position: Int) = values[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_community_spinner, parent, false)
            val viewHolder = ViewHolder(rowView)
            rowView.tag = viewHolder
        }

        val viewHolder = rowView!!.tag as ViewHolder
        viewHolder.bind(values[position])

        return rowView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView = convertView
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_community_spinner_down, parent, false)
            val viewHolder = ViewHolderDropDown(rowView)
            rowView.tag = viewHolder
        }

        val viewHolder = rowView!!.tag as ViewHolderDropDown
        viewHolder.bind(values[position])

        return rowView
    }

    inner class ViewHolder(val itemView: View) {
        fun bind(community: PresentationCommunityModel) {
            itemView.apply {
                name.text = community.title
            }
        }
    }

    inner class ViewHolderDropDown(val itemView: View) {
        fun bind(community: PresentationCommunityModel) {
            itemView.apply {
                name.text = community.title
                name.isSelected = spinner.selectedItem == community
            }
        }
    }

}