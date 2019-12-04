package com.doneit.ascend.presentation.main.home.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.daily.DailyFragment
import com.doneit.ascend.presentation.main.home.HomeFragment

class TabAdapter(
    fragmentManager: FragmentManager,
    private val fragments: ArrayList<Fragment> = arrayListOf(),
    private val titles: ArrayList<String> = arrayListOf()
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

//    val onPageChangeListener = object : ViewPager.OnPageChangeListener {
//        override fun onPageScrollStateChanged(state: Int) {
//
//        }
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//
//        }
//
//        override fun onPageSelected(position: Int) {
////            this@TabAdapter.onPageSelected(position)
//        }
//    }

//    fun onPageSelected(position: Int) {
//        if (fragments[position] is IViewPageOnSelectSubscriber) {
//            (fragments[position] as IViewPageOnSelectSubscriber).initSearchListener()
//        }
//    }

    companion object {
        fun newInstance(fragment: Fragment, fragmentManager: FragmentManager): TabAdapter {
            var fragments: ArrayList<Fragment> = arrayListOf(
                DailyFragment(),
                DailyFragment(),
                DailyFragment(),
                DailyFragment()
            )

            val titles: ArrayList<String> = arrayListOf(
                fragment.getString(R.string.daily),
                fragment.getString(R.string.webinar),
                fragment.getString(R.string.recovery),
                fragment.getString(R.string.master_mind)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }
    }
}