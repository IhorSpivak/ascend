package com.doneit.ascend.presentation.video_chat.attachments.add_url

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAddUrlBinding
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract
import org.kodein.di.generic.instance

class AddUrlFragment : BaseFragment<FragmentAddUrlBinding>() {

    override val viewModelModule = AddUrlViewModelModule.get(this)
    override val viewModel: AddUrlContract.ViewModel by instance()
    private val router: AddUrlContract.Router by instance()
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })
    }

    private fun handleNavigation(action: AttachmentsContract.Navigation) {
        when (action) {
            AttachmentsContract.Navigation.BACK -> router.onBack()
        }
    }



    companion object {

        fun newInstance(): AddUrlFragment {
            return AddUrlFragment()
        }
    }


}