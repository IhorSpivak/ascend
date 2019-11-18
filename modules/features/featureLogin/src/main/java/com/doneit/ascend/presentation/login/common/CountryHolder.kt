package com.doneit.ascend.presentation.login.common

import android.view.View
import com.rilixtech.widget.countrycodepicker.Country
import kotlinx.android.synthetic.main.list_item_country.view.*

class CountryHolder(private val itemView: View) {
    fun bind(country: Country) {
        itemView.apply {
            tvCode.text = country.phoneCode
            tvCountry.text = country.name
            tvCountry.post {
                tvCountry.isSingleLine = false
            }
        }
    }
}