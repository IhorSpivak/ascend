package com.doneit.ascend.presentation.main.home.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.parseTo
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
        fun newInstance(fragment: Fragment, fragmentManager: FragmentManager, userCommunity: String?): TabAdapter {

            val fragments: ArrayList<Fragment> = arrayListOf(
                getFragment(GroupType.DAILY, isMineGroups = true),
                getFragment(GroupType.WEBINARS, isMineGroups = null),
                getFragment(userCommunity.parseTo(), isMineGroups = null),
                getFragment(GroupType.MASTER_MIND, isMineGroups = null)
            )

            val titles: ArrayList<String> = arrayListOf(
                fragment.getString(R.string.daily),
                fragment.getString(R.string.webinars),
                userCommunity ?: "",
                fragment.getString(R.string.master_mind)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }

        private fun getFragment(groupType: GroupType?, isMineGroups: Boolean?): Fragment {
            val args = GroupsArgs(groupType, isMineGroups)

            val fragment = GroupsFragment()
            (fragment as Fragment).arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, args)
            }

            return fragment
        }
    }
}