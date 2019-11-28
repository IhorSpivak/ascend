package com.doneit.ascend.presentation.login.web_page

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.presentation.login.web_page.common.WebPageArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel

interface WebPageContract {
    interface ViewModel : ArgumentedViewModel<WebPageArgs> {
        val title: LiveData<String>
        val content: LiveData<PageEntity>

        fun onBackClick()
    }

    interface Router {
        fun goBack()
    }
}