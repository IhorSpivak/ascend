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

        val groupId = arguments!!.getLong(GROUP_ID_KEY)
        viewModel.init(groupId)

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
        private const val GROUP_ID_KEY = "GROUP_ID_KEY"

        fun newInstance(groupId: Long): AddUrlFragment {
            val fragment = AddUrlFragment()

            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID_KEY, groupId)
            }

            return fragment
        }
    }


}