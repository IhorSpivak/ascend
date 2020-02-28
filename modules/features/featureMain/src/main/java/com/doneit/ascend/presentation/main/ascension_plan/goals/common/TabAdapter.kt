package com.doneit.ascend.presentation.main.ascension_plan.goals.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.GoalsListFragment
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.common.GoalsListArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment

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
                context.getString(R.string.goals_tab_active),
                context.getString(R.string.goals_tab_completed)
            )

            return TabAdapter(fragmentManager, fragments, titles)
        }
        private fun getFragment(isCompleted: Boolean?): Fragment {
            val args = GoalsListArgs(isCompleted)

            val fragment = GoalsListFragment()
            (fragment as Fragment).arguments = Bundle().apply {
                putParcelable(ArgumentedFragment.KEY_ARGS, args)
            }

            return fragment
        }
        //use later to create base adapter and use as common
        /*private inline fun <reified T: Fragment> getFragment2(isCompleted: Boolean?, instance: () -> T): Fragment{
            return instance().apply {
                (this as Fragment).arguments = Bundle().apply {
                    putParcelable(ArgumentedFragment.KEY_ARGS, GoalsListArgs(isCompleted))
                }
            }
        }*/
    }
}