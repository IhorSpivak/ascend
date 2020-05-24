package com.doneit.ascend.presentation.video_chat_webinar.questions

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentQuestionsBinding
import org.kodein.di.generic.instance

class QuestionFragment: BaseFragment<FragmentQuestionsBinding>(){
    override val viewModel: QuestionContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

}