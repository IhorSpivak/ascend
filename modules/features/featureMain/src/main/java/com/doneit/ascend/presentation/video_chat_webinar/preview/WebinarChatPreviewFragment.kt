package com.doneit.ascend.presentation.video_chat_webinar.preview

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarChatPreviewBinding
import com.doneit.ascend.presentation.utils.extensions.hide
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.WebinarVideoChatInProgressFragment
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.common.FourQuestionsAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WebinarChatPreviewFragment : BaseFragment<FragmentWebinarChatPreviewBinding>() {

    private val adapter: FourQuestionsAdapter by lazy { FourQuestionsAdapter() }

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarChatPreviewContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: WebinarChatPreviewContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.send.setOnClickListener {
            if (binding.message.text.isNotBlank()) {
                viewModel.createQuestion(binding.message.text.trim().toString())
                binding.message.text.clear()
            }
        }
        binding.rvQuestions.adapter = adapter
        observeEvents()
        addAdapterDataObserver()
    }

    private fun observeEvents() {
        viewModel.questions.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.showMessgeSent.observe(this, Observer {
            binding.questionSent.show()
            binding.questionSent.postDelayed({
                if (isVisible) {
                    binding.questionSent.hide()
                }
            }, WebinarVideoChatInProgressFragment.DELAY_HIDE_SEND_BADGE)
        })
    }

    private fun addAdapterDataObserver() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 0) {
                    (binding.rvQuestions.layoutManager as LinearLayoutManager).scrollToPosition(
                        0
                    )
                }
            }
        })
    }
}