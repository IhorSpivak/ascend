package com.doneit.ascend.presentation.profile.change_location.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.doneit.ascend.presentation.main.R
import com.rilixtech.widget.countrycodepicker.Country
import kotlinx.android.synthetic.main.view_spinner_title.view.*


class CountriesAdapter(
    val countries: List<Country>
) : BaseAdapter() {

    override fun getItemId(p0: Int) = 0L

    override fun getItem(position: Int): Country? = countries[position]

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_spinner_title, parent, false)

        view?.let {
            it.title.hint = view.resources.getString(R.string.select_country)
            it.title.text = countries[position].name
        }
        "".isNullOrBlank()
        return view
    }

    override fun getCount(): Int = countries.size

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
                    .inflate(R.layout.list_item_country, parent, false)
            }

        view?.let {
            if (it.tag != null) {
                (it.tag as CountryHolder).bind(countries[position].name)
            } else {
                val holder = CountryHolder(view)
                holder.bind(countries[position].name)
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