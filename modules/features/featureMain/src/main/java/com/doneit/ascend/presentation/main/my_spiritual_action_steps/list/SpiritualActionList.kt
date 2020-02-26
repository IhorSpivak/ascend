package com.doneit.ascend.presentation.main.my_spiritual_action_steps.list

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMySpiritualActionStepsListBinding
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.common.SpiritualActionListArgs
import org.kodein.di.generic.instance

class SpiritualActionList: BaseFragment<FragmentMySpiritualActionStepsListBinding>() {
    override val viewModelModule = SpiritualActionListViewModelModule.get(this)
    override val viewModel: SpiritualActionListContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        val args = arguments!!.getParcelable<SpiritualActionListArgs>(ArgumentedFragment.KEY_ARGS)!!
        viewModel.applyArguments(args)
    }
}