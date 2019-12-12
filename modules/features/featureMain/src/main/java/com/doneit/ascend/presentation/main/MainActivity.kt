package com.doneit.ascend.presentation.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.common.BottomNavigationAdapter
import com.doneit.ascend.presentation.utils.Constants.TYPE_MASTER_MIND
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MainActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("MainActivity") {
        bind<MainRouter>() with provider {
            MainRouter(
                this@MainActivity,
                instance()
            )
        }
    }

    private val router: MainRouter by instance()
    private val userUseCase: UserUseCase by instance()

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationListener()

        fabCreateGroup.setOnClickListener {
            router.navigateToCreateGroup()
        }

        router.navigateToHome()

        GlobalScope.launch(Dispatchers.Main) {
            val user = userUseCase.getUserLive()

            user.observe(this@MainActivity, Observer {
                setCreateGroupState(it?.role == TYPE_MASTER_MIND)
            })
        }
    }

    private fun setNavigationListener() {
        with(BottomNavigationAdapter(router)) {
            attach(mainBottomNavigationView)
        }
    }

    private fun setCreateGroupState(isEnabled: Boolean) {
        fabCreateGroup.isEnabled = isEnabled
    }
}