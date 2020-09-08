package com.doneit.ascend.presentation.main.home.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.home.daily.DailyFragment
import com.doneit.ascend.presentation.main.home.groups_list.GroupsListFragment

class TabAdapter(
    fragmentManager: FragmentManager,
    private val fragments: List<() -> Fragment> = arrayListOf(),
    private val titles: List<String> = arrayListOf()
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]()
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            titles: List<String>
        ): TabAdapter {

            val fragments: ArrayList<() -> Fragment> = arrayListOf(
                { DailyFragment() },
                { GroupsListFragment.newInstance(groupType = GroupType.WEBINAR, groupStatus = GroupStatus.UPCOMING, sortType = SortType.ASC) },
                { GroupsListFragment.newInstance(groupType = GroupType.SUPPORT, groupStatus = GroupStatus.UPCOMING, sortType = SortType.ASC) },
                { GroupsListFragment.newInstance(groupType = GroupType.MASTER_MIND, groupStatus = GroupStatus.UPCOMING, sortType = SortType.ASC) }
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }
    }
}