package com.doneit.ascend.presentation.splash

import android.animation.Animator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.vrgsoft.core.presentation.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("SplashActivity") {
    }

    private val router: ISplashRouter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        textView.alpha = 0F

        textView.animate()
            .setDuration(100)
            .setInterpolator(LinearInterpolator())
            .alpha(1F)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator?) {
                    router.goToLogin()
                }

                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}
            })
            .start()
    }
}