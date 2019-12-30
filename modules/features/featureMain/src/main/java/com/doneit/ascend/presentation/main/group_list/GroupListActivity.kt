package com.doneit.ascend.presentation.main.group_list

import android.os.Bundle
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
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

        val args = intent.getParcelableExtra<GroupListArgs>(ARG_GROUP_FILTER)!!
        router.navigateToStart(args)
    }

    companion object {
        const val ARG_GROUP_FILTER = "GROUP_FILTER"
    }
}