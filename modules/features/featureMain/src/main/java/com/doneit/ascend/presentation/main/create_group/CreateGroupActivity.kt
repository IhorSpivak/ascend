package com.doneit.ascend.presentation.main.create_group

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import kotlinx.android.synthetic.main.activity_create_group.*
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
    }

    private val router: CreateGroupRouter by instance()

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        router.navigateToSelectGroupType()
    }

    fun clearBackground() {
        window.setBackgroundDrawable(null)
    }

    fun restoreBackground() {
        window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(applicationContext, android.R.color.white)))
    }
}