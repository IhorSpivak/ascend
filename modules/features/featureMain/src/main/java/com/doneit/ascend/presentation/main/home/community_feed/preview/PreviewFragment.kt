package com.doneit.ascend.presentation.main.home.community_feed.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentPreviewBinding
import com.doneit.ascend.presentation.main.home.community_feed.preview.common.PreviewAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class PreviewFragment : BaseFragment<FragmentPreviewBinding>() {
    override val viewModel: PreviewContract.ViewModel by instance()
    override val viewModelModule: Kodein.Module
        get() = PreviewViewModelModule.get(this)

    override val kodeinModule: Kodein.Module = Kodein.Module(this::class.java.simpleName) {
        bind(tag = KEY_ATTACHMENTS) from provider {
            requireArguments().getParcelableArrayList<Attachment>(KEY_ATTACHMENTS)!!
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.vpMedia.adapter =
            PreviewAdapter(requireArguments().getParcelableArrayList(KEY_ATTACHMENTS)!!)
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = ""
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onDestroyView() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(null)
        }
        super.onDestroyView()
    }

    companion object {

        internal const val KEY_ATTACHMENTS = "key_attachments"

        fun newInstance(attachments: Collection<Attachment>) = PreviewFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(KEY_ATTACHMENTS, ArrayList(attachments))
            }
        }
    }
}