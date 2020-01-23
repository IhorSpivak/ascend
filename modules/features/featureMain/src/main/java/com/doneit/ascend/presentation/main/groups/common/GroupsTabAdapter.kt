package com.doneit.ascend.presentation.main.groups.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.domain.entity.dto.GroupStatus
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.groups.GroupsArg
import com.doneit.ascend.presentation.main.groups.group_list.GroupListArgs
import com.doneit.ascend.presentation.main.groups.group_list.GroupListFragment

class GroupsTabAdapter(
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
        fun newInstance(context: Context, fragmentManager: FragmentManager, args: GroupsArg): GroupsTabAdapter {
            val fragments: ArrayList<Fragment> = arrayListOf(
                getFragment(args.toGroupListArgs(SortType.ASC, GroupStatus.UPCOMING)),
                getFragment(args.toGroupListArgs(SortType.DESC, GroupStatus.ENDED))
            )

            val titles: ArrayList<String> = arrayListOf(
                context.getString(R.string.upcoming),
                context.getString(R.string.past)
            )

            return GroupsTabAdapter(fragmentManager, fragments, titles)
        }

        private fun getFragment(filter: GroupListArgs): Fragment {
            val fragment = GroupListFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, filter)
            }

            return fragment
        }
    }
}