package com.doneit.ascend.presentation.splash

import android.animation.Animator
import android.net.Uri
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vrgsoft.core.presentation.activity.BaseActivity
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SplashActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("SplashActivity") {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SplashViewModel::class.java.simpleName) with provider {
            SplashViewModel(
                instance()
            )
        }
        bind<SplashContract.ViewModel>() with provider { vm<SplashViewModel>(instance()) }
    }

    private val router: ISplashRouter by instance()
    private val viewModel: SplashContract.ViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val extras = intent?.extras?: Bundle()
        imageLogo.alpha = 0F
        intent?.data?.let {
            openDeepLink(it)
        } ?: if (intent?.extras?.containsKey(KEY_GROUP_ID) == true){
            router.goToLogin(extras)
        }else{
            imageLogo.animate()
                .setDuration(100)
                .setInterpolator(LinearInterpolator())
                .alpha(1F)
                .setListener(object : Animator.AnimatorListener {

                    override fun onAnimationEnd(animation: Animator?) {
                        GlobalScope.launch {
                            delay(2900)
                            router.goToLogin(extras)
                        }
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}
                })
                .start()
        }
    }

    private inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM {
        return ViewModelProviders.of(this, factory)[VM::class.java]
    }

    private fun openDeepLink(data: Uri) {
        var path0 = "";

        if (data.pathSegments.size >= 1) {
            path0 = data.pathSegments[0]
            when(path0){
                DEEP_LINK_TYPE_GROUP -> {
                    val bundle = Bundle().apply {
                        putLong(KEY_GROUP_ID, data.pathSegments[1].toLong())
                    }
                    router.goToLogin(bundle)
                }
                DEEP_LINK_TYPE_PROFILE -> {
                    val bundle = Bundle().apply {
                        putLong(KEY_PROFILE_ID, data.pathSegments[1].toLong())
                    }
                    router.goToLogin(bundle)
                }
            }
        }
    }


    companion object{
        const val DEEP_LINK_TYPE_PROFILE = "profile"
        const val DEEP_LINK_TYPE_GROUP = "group"
        const val DEEP_LINK = "ascend.com"
        const val KEY_GROUP_ID = "group_id"
        const val KEY_PROFILE_ID = "profile_id"
    }
}