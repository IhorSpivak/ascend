package com.doneit.ascend.presentation.main.home.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.home.channels.ChannelsFragment
import com.doneit.ascend.presentation.main.home.groups.GroupsFragment
import com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams.MMLiveStreamsFragment
import com.doneit.ascend.presentation.main.master_mind_info.mm_content.posts.MMPostsFragment

class MMProfileTabAdapter(
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
        //todo change fragments when their be ready
        //todo for example
        fun newInstance(
            fragmentManager: FragmentManager,
            userId: Long,
            user: UserEntity,
            titles: List<String>
        ): MMProfileTabAdapter {

            val fragments: ArrayList<() -> Fragment> = arrayListOf(
                { MMLiveStreamsFragment.newInstance(userId,user) },
                { GroupsFragment.newInstance(userId, GroupType.SUPPORT) },
                { ChannelsFragment.getInstance() },
                { ChannelsFragment.getInstance() },
                { MMPostsFragment.newInstance(userId, user) },
                { ChannelsFragment.getInstance() }
            )

            return MMProfileTabAdapter(fragmentManager, fragments, titles)
        }
    }


}