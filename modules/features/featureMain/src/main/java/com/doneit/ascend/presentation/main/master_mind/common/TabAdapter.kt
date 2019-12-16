package com.doneit.ascend.presentation.main.master_mind.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.master_mind.list.ListFragment
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs

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

    companion object {
        fun newInstance(context: Context, fragmentManager: FragmentManager): TabAdapter {

            var fragments: ArrayList<Fragment> = arrayListOf(
                getFragment(false),
                getFragment(true)
            )

            val titles: ArrayList<String> = arrayListOf(
                context.getString(R.string.daily),
                context.getString(R.string.webinar),
                context.getString(R.string.recovery),
                context.getString(R.string.master_mind)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }

        private fun getFragment(isFollowing: Boolean): Fragment {
            val args = ListArgs(isFollowing)

            val fragment = ListFragment()
            (fragment as Fragment).arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, args)
            }

            return fragment
        }
    }
}