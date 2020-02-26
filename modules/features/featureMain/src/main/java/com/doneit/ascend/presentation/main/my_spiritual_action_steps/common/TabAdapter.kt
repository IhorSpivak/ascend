package com.doneit.ascend.presentation.main.my_spiritual_action_steps.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.SpiritualActionList
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.common.SpiritualActionListArgs

class TabAdapter(
    private val fragmentManager: FragmentManager,
    private val fragments: ArrayList<Fragment> = arrayListOf(),
    private val titles: ArrayList<String> = arrayListOf()
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    companion object{
        fun newInstance(context: Context, fragmentManager: FragmentManager): TabAdapter{
            val fragments: ArrayList<Fragment> = arrayListOf(
                getFragment(null),
                getFragment(true)
            )

            val titles: ArrayList<String> = arrayListOf(
                context.getString(R.string.tab_active),
                context.getString(R.string.tab_completed)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }
        private fun getFragment(isFollowing: Boolean?): Fragment {
            val args = SpiritualActionListArgs(isFollowing)

            val fragment = SpiritualActionList()
            (fragment as Fragment).arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, args)
            }

            return fragment
        }
    }
}