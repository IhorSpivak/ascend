package com.doneit.ascend.presentation.video_chat.add_mm_plan

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAddPlanBinding
import org.kodein.di.generic.instance

class AddPlanFragment : BaseFragment<FragmentAddPlanBinding>() {

    override val viewModelModule = AddPlanViewModelModule.get(this)
    override val viewModel: AddPlanContract.ViewModel by instance()

    private val router: AddPlanContract.Router by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })
    }

    private fun handleNavigation(action: AddPlanContract.Navigation) {
        when (action) {
            AddPlanContract.Navigation.BACK -> router.onBack()
        }
    }

    companion object {
        private const val GROUP_ID_KEY = "GROUP_ID_KEY"

        fun newInstance(groupId: Long): AddPlanFragment {
            val fragment = AddPlanFragment()
            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID_KEY, groupId)
            }

            return fragment
        }
    }
}