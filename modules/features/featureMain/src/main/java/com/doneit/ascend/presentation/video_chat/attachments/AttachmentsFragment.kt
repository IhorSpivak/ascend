package com.doneit.ascend.presentation.video_chat.attachments

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAttachmentsBinding
import com.doneit.ascend.presentation.main.search.common.SearchAdapter
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import org.kodein.di.generic.instance

class AttachmentsFragment : BaseFragment<FragmentAttachmentsBinding>() {


    override val viewModelModule = AttachmentsViewModelModule.get(this)
    override val viewModel: AttachmentsContract.ViewModel by instance()

    private val adapter: AttachmentsAdapter by lazy {
        AttachmentsAdapter({
            //TODO:
        }, {
            //TODO:
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.adapter = this.adapter
        binding.model = viewModel
    }

    companion object {
        private const val ATTACHMENTS_ARGS = "ATTACHMENTS_ARGS"

        fun newInstance(args: AttachmentsArg): AttachmentsFragment {
            val fragment = AttachmentsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ATTACHMENTS_ARGS, args)
            }
            return fragment
        }
    }
}