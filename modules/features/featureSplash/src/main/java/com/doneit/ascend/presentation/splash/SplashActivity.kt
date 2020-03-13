package com.doneit.ascend.presentation.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
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
    private var isFromSavedState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        isFromSavedState = savedInstanceState?.getBoolean(SAVED_STATE, false)?: false

        val extras = intent?.extras?: Bundle()
        tvTitle.alpha = 0F

        if (isFromSavedState){
            router.goToLogin(extras)
        }else{
            tvTitle.animate()
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

            tvSubtitle.animate()
                .setDuration(100)
                .setInterpolator(LinearInterpolator())
                .alpha(1F)
                .start()

            viewModel.user.observe(this, Observer {
                it?.let {
                    val title =
                        if (it.isMasterMind) getString(R.string.mastermind_subtitle) else it.community
                    tvSubtitle.text = title
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SAVED_STATE, true)
        super.onSaveInstanceState(outState)
    }

    private inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM {
        return ViewModelProviders.of(this, factory)[VM::class.java]
    }
    companion object{
        private const val SAVED_STATE = "saved_state"
    }
}