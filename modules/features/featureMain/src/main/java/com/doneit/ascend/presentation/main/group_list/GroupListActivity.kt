package com.doneit.ascend.presentation.main.group_list

import android.os.Bundle
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class GroupListActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("GroupListActivity") {
        bind<GroupListRouter>() with singleton {
            GroupListRouter(
                this@GroupListActivity
            )
        }
    }

    private val router: GroupListRouter by instance()

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val groupType = intent.getIntExtra(ARG_GROUP_TYPE, GroupType.MASTER_MIND.ordinal)
        val isMyGroups = intent.getIntExtra(ARG_IS_MINE, 0)
        val isAllGroups = intent.getBooleanExtra(ARG_IS_ALL, false)

        router.navigateToStart(groupType, isMyGroups, isAllGroups)
    }

    companion object {
        const val ARG_GROUP_TYPE = "arg_group_type"
        const val ARG_IS_MINE = "arg_is_mine"
        const val ARG_IS_ALL = "arg_is_all"
    }
}