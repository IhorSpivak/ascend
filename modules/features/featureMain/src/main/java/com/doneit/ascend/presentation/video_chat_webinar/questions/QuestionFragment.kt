package com.doneit.ascend.presentation.video_chat_webinar.questions

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentQuestionsBinding
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.questions.common.QuestionAdapter
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class QuestionFragment : BaseFragment<FragmentQuestionsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<QuestionContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    private val adapter: QuestionAdapter by lazy { QuestionAdapter() }

    override val viewModel: QuestionContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.rvQuestions.adapter = adapter
        applyDataObserver()

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            emptyList.visible(it.isNullOrEmpty())
            adapter.submitList(it)
        })
    }

    private fun applyDataObserver() {
            adapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (itemCount > 0) {
                        scrollIfNeed()
                    }
                }
            })
    }

    private fun scrollIfNeed() {
        adapter.let {
            val lm =
                binding.rvQuestions.layoutManager as LinearLayoutManager
            val first = lm.findFirstVisibleItemPosition()
            if (first < 5) binding.rvQuestions.scrollToPosition(0)
        }
    }

}