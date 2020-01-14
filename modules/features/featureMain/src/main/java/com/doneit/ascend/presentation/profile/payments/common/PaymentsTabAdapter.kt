package com.doneit.ascend.presentation.profile.payments.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.profile.payments.payment_methods.PaymentMethodsFragment

class PaymentsTabAdapter(
    fragmentManager: FragmentManager,
    private val items: ArrayList<TabAdapterItem> = arrayListOf()
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return items[position].fragment
    }

    override fun getCount() = items.size

    override fun getPageTitle(position: Int): CharSequence? {
        return items[position].title
    }

    companion object {
        fun newInstance(fragment: Fragment, isMasterMind: Boolean): PaymentsTabAdapter {
            val items: ArrayList<TabAdapterItem> = arrayListOf(
                TabAdapterItem(fragment.getString(R.string.earnings), PaymentMethodsFragment()),
                TabAdapterItem(fragment.getString(R.string.payments_methods), PaymentMethodsFragment()),
                TabAdapterItem(fragment.getString(R.string.in_app_purchases), PaymentMethodsFragment())
            )

            if(isMasterMind) {
                items.removeAt(2)
            } else {
                items.removeAt(0)
            }

            return PaymentsTabAdapter(fragment.childFragmentManager, items = items)
        }

    }
}