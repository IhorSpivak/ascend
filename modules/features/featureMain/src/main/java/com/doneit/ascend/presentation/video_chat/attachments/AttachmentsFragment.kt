package com.doneit.ascend.presentation.video_chat.attachments

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAttachmentsBinding
import com.doneit.ascend.presentation.utils.extensions.copyToClipboard
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import org.kodein.di.generic.instance

class AttachmentsFragment : BaseFragment<FragmentAttachmentsBinding>() {


    override val viewModelModule = AttachmentsViewModelModule.get(this)
    override val viewModel: AttachmentsContract.ViewModel by instance()

    private val adapter: AttachmentsAdapter by lazy {
        AttachmentsAdapter({
            //TODO: implement
            Toast.makeText(requireContext(), "download", Toast.LENGTH_LONG).show()
        }, {
            //TODO: refactor
            it.link.copyToClipboard(requireContext())
            Toast.makeText(requireContext(), "copy", Toast.LENGTH_LONG).show()
        },{
            viewModel.onDelete(it)
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.adapter = this.adapter
        binding.model = viewModel
        val decorator = TopListDecorator(resources.getDimension(R.dimen.attachments_list_top_padding).toInt())
        binding.rvAttachments.addItemDecoration(decorator)

        viewModel.attachments.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
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