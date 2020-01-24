package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeContract
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CreateGroupActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("CreateGroupActivity") {
        bind<CreateGroupRouter>() with provider {
            CreateGroupRouter(
                this@CreateGroupActivity
            )
        }

        bind<CreateGroupContract.Router>() with provider {
            CreateGroupRouter(
                this@CreateGroupActivity
            )
        }

        bind<SelectGroupTypeContract.Router>() with provider {
            CreateGroupRouter(
                this@CreateGroupActivity
            )
        }

        bind<CalendarPickerUtil>() with provider {
            CalendarPickerUtil(
                instance()
            )
        }
    }

    private val router: CreateGroupRouter by instance()

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        router.navigateToSelectGroupType()
    }
}