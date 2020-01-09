package com.doneit.ascend.presentation.profile.payments.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.profile.payments.payment_methods.PaymentMethodsFragment

class PaymentsTabAdapter(
    fragmentManager: FragmentManager,
    private val titles: ArrayList<String> = arrayListOf()
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val paymentsFragment = PaymentMethodsFragment()
    private val purchases = PaymentMethodsFragment()

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> paymentsFragment
            else -> purchases
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    companion object {
        fun newInstance(fragment: Fragment): PaymentsTabAdapter {
            val titles: ArrayList<String> = arrayListOf(
                fragment.getString(R.string.payments_methods),
                fragment.getString(R.string.in_app_purchases)
            )

            return PaymentsTabAdapter(fragment.childFragmentManager, titles = titles)
        }

    }
}