package com.doneit.ascend.ui

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.doneit.ascend.R
import com.doneit.ascend.ui.BaseFragment
import kotlinx.android.synthetic.main.splash_fragment.*

class SplashFragment : BaseFragment(R.layout.splash_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun toLogin() {
            val extras = FragmentNavigatorExtras(textView to "logo")
            findNavController().navigate(R.id.loginFragment, null, null, extras)
        }

        textView.alpha = 0F

        textView.animate()
            .setDuration(100)
            .setInterpolator(LinearInterpolator())
            .alpha(1F)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator?) {
                    toLogin()
                }

                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}
            })
            .start()

    }

}
