package com.doneit.ascend.presentation.login.web_page

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentWebPageBinding
import com.doneit.ascend.presentation.login.web_page.common.WebPageArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.showDefaultError
import com.doneit.ascend.presentation.utils.showInfoDialog
import kotlinx.android.synthetic.main.fragment_web_page.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

class WebPageFragment : ArgumentedFragment<FragmentWebPageBinding, WebPageArgs>() {

    override val viewModelModule = WebPageViewModelModule.get(this)
    override val viewModel: WebPageContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()

        wvContent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, request.url)
                view.context.startActivity(intent)
                return true
            }
        }

        val webSetting = wvContent.settings
        webSetting.javaScriptEnabled = false
        webSetting.displayZoomControls = true

        viewModel.content.observe(this, Observer {
            it?.let { pageIt ->
                wvContent.loadData(pageIt.content, "text/html;", "UTF-8")
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
            Messages.EROR.getId() -> {
                showDefaultError(message.content!!)
            }
        }
    }
}