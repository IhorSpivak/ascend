package com.doneit.ascend.presentation.views.phone_code.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
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
            it.title.text = view.resources.getString(R.string.phone_format, countries[position].phoneCode)
        }
        return view
    }

    override fun getCount(): Int = countries.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater
            .from(parent?.context)
            .inflate(R.layout.list_item_country_code, parent, false)

        view?.let {
            if (it.tag != null) {
                (it.tag as CountryHolder).bind(countries[position])
            } else {
                val holder = CountryHolder(view)
                holder.bind(countries[position])
                it.tag = holder
            }
            return it
        }

        return view
    }

    fun getPositionByIso(iso: String): Int {
        return countries.indexOfFirst { it.iso == iso }
    }

    fun getPositionByCode(code: String): Int {
        return countries.indexOfFirst { it.phoneCode == code }
    }
}