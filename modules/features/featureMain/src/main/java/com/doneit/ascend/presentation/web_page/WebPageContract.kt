package com.doneit.ascend.presentation.web_page

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.web_page.common.WebPageArgs

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