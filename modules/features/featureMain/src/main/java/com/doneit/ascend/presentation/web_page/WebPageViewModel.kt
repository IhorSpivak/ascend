package com.doneit.ascend.presentation.web_page

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.use_case.interactor.page.PageUseCase
import com.doneit.ascend.presentation.web_page.common.WebPageArgs
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class WebPageViewModel(
    private val pageUseCase: PageUseCase,
    private val router: WebPageContract.Router
) : BaseViewModelImpl(), WebPageContract.ViewModel {

    override val title = MutableLiveData<String>()
    override val content = MutableLiveData<PageEntity>()

    override fun applyArguments(args: WebPageArgs) {
        title.postValue(args.title)

        GlobalScope.launch {
            val result = pageUseCase.getPage(args.pageType)

            if(result.isSuccessful) {
                content.postValue(result.successModel)
            }
            else {
                onBackClick()
            }
        }
    }

    override fun onBackClick() {
        router.onBack()
    }
}