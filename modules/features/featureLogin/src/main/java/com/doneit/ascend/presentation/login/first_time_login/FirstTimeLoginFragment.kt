package com.doneit.ascend.presentation.login.first_time_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.GridRadioGroup
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentFirstTimeLoginBinding
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionsAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.TemplateAnswerItemBinding
import com.doneit.ascend.presentation.main.databinding.TemplateSelectAnswerItemBinding
import kotlinx.android.synthetic.main.fragment_first_time_login.*
import org.kodein.di.generic.instance

class FirstTimeLoginFragment : BaseFragment<FragmentFirstTimeLoginBinding>() {

    override val viewModelModule = FirstTimeLoginViewModelModule.get(this)
    override val viewModel: FirstTimeLoginContract.ViewModel by instance()

    private val adapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf(), viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        rvQuestions.adapter = adapter

        binding.toolbar.imBack.visibility = View.INVISIBLE
        showProgress(true)

        viewModel.questions.observe(this, Observer {
            it?.let {
                adapter.updateData(it)

                val questionBinding: TemplateSelectAnswerItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(communityBox.context),
                    R.layout.template_select_answer_item,
                    communityBox,
                    false
                )

                with(questionBinding) {
                    this.title = it.community?.title

                    it.community?.answerOptions?.forEach { optionValue ->
                        val binding =
                            TemplateAnswerItemBinding.inflate(LayoutInflater.from(questionBinding.root.context))

                        binding.title = optionValue
                        binding.rbOption.setOnCheckedChangeListener { view, isChecked ->
                            if (isChecked) {
                                (rgOptions as GridRadioGroup).clickByRadioButton(view)

                                viewModel.setCommunity(optionValue)
                            }
                        }

                        rgOptions.addView(binding.root, getOptionViewParams())
                    }
                }

                communityBox.removeAllViews()
                communityBox.addView(questionBinding.root)
                showProgress(false)
            }
        })


    }

    private fun getOptionViewParams(): GridLayout.LayoutParams {
        val param = GridLayout.LayoutParams(
            GridLayout.spec(
                GridLayout.UNDEFINED, GridLayout.FILL, 1f
            ),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        )
        param.width = 0

        return param
    }
}