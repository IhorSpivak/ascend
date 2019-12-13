package com.doneit.ascend.presentation.main.search

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.MainRouter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivitySearchBindingImpl
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SearchActivity: BaseActivity() {
    override fun diModule() = Kodein.Module("LogInActivity") {
        bind<SearchRouter>() with singleton {
            SearchRouter(
                this@SearchActivity
            )
        }

        bind<SearchContract.Router>() with singleton { instance<SearchRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SearchViewModel::class.java.simpleName) with provider {
            SearchViewModel(
                instance()
            )
        }
        bind<SearchContract.ViewModel>() with provider { vm<SearchViewModel>(instance()) }
    }

    private val viewModel: SearchContract.ViewModel by instance()
    private lateinit var binding: ActivitySearchBindingImpl

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }
    }
}