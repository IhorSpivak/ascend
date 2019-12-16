package com.doneit.ascend.presentation.main.home.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.home.group.GroupsFragment
import com.doneit.ascend.presentation.main.home.group.common.GroupsArgs

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
        fun newInstance(fragment: Fragment, fragmentManager: FragmentManager): TabAdapter {

            var fragments: ArrayList<Fragment> = arrayListOf(
                getFragment(GroupType.DAILY, isMineGroups = true, isAllGroups = true),
                getFragment(GroupType.WEBINAR, isMineGroups = null, isAllGroups = false),
                getFragment(GroupType.RECOVERY, isMineGroups = null, isAllGroups = false),
                getFragment(GroupType.MASTER_MIND, isMineGroups = null, isAllGroups = false)
            )

            val titles: ArrayList<String> = arrayListOf(
                fragment.getString(R.string.daily),
                fragment.getString(R.string.webinar),
                fragment.getString(R.string.recovery),
                fragment.getString(R.string.master_mind)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }

        private fun getFragment(groupType: GroupType, isMineGroups: Boolean?, isAllGroups: Boolean): Fragment {
            val args = GroupsArgs(groupType.ordinal, isMineGroups, isAllGroups)

            val fragment = GroupsFragment()
            (fragment as Fragment).arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, args)
            }

            return fragment
        }
    }
}