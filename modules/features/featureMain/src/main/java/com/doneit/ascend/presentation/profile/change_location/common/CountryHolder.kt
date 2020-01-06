package com.doneit.ascend.presentation.profile.change_location.common

import android.view.View
import kotlinx.android.synthetic.main.list_item_country_code.view.*

class CountryHolder(private val itemView: View) {
    fun bind(country: String) {
        itemView.apply {
            tvCountry.text = country
        }
    }
}