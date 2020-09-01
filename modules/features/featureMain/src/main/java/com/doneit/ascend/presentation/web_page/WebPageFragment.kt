package com.doneit.ascend.presentation.web_page

import android.os.Bundle
import android.text.Html
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebPageBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.showDefaultError
import com.doneit.ascend.presentation.utils.showInfoDialog
import com.doneit.ascend.presentation.web_page.common.WebPageArgs
import kotlinx.android.synthetic.main.fragment_web_page.*
import org.kodein.di.generic.instance

class WebPageFragment : BaseFragment<FragmentWebPageBinding>() {

    override val viewModelModule = WebPageViewModelModule.get(this)
    override val viewModel: WebPageContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()
        arguments?.let { arguments ->
            viewModel.getPage(WebPageArgs(getString(arguments.getInt(PAGE_TITLE)), requireArguments().getString(PAGE_TYPE).toString()))
        }

        viewModel.content.observe(this, Observer {
            it?.let { pageIt ->
                page_content.text = (HtmlCompat.fromHtml(pageIt.content, HtmlCompat.FROM_HTML_MODE_LEGACY))
            }
        })

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

    }

    override fun handleSuccessMessage(message: PresentationMessage) {
        when(message.id) {
            Messages.PASSWORD_SENT.getId() -> {
                showInfoDialog(
                    getString(R.string.title_password_sent),
                    getString(R.string.content_password_sent)
                )
            }
        }
    }

    override fun handleErrorMessage(message: PresentationMessage) {
        when(message.id) {
            Messages.DEFAULT_ERROR.getId() -> {
                showDefaultError(message.content!!)
            }
        }
    }

    companion object {
        const val PAGE_TYPE = "PAGE_TYPE"
        const val PAGE_TITLE = "PAGE_TITLE"

        fun newInstance(title: Int?, page_type: String?): WebPageFragment {
            val fragment = WebPageFragment()
            fragment.arguments = Bundle().apply {
                title?.let { putInt(PAGE_TITLE, title) }
                putString(PAGE_TYPE, page_type)
            }
            return fragment
        }

    }

}