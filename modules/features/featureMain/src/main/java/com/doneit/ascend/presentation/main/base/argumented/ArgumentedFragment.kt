package com.doneit.ascend.presentation.main.base.argumented

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.vrgsoft.core.presentation.fragment.argumented.Argumented
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments

abstract class ArgumentedFragment<B : ViewDataBinding, A : BaseArguments>
    : BaseFragment<B>(), Argumented<A> {
    abstract override val viewModel: ArgumentedViewModel<A>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments!!.getParcelable<A>(KEY_ARGS)
        if (args != null) {
            viewModel.applyArguments(args)
        }
    }

    companion object {
        const val KEY_ARGS = "key_args"
    }
}