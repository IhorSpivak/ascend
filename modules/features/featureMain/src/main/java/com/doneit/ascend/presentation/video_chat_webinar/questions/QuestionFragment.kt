package com.doneit.ascend.presentation.video_chat_webinar.questions

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentQuestionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.questions.common.QuestionAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class QuestionFragment: BaseFragment<FragmentQuestionsBinding>(){

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

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}