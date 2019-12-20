package com.doneit.ascend.presentation.main.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivitySearchBindingImpl
import com.doneit.ascend.presentation.main.search.common.SearchAdapter
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SearchActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("SearchActivity") {
        bind<SearchRouter>() with singleton {
            SearchRouter(
                this@SearchActivity
            )
        }

        bind<SearchContract.Router>() with singleton { instance<SearchRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SearchViewModel::class.java.simpleName) with provider {
            SearchViewModel(
                instance(),
                instance()
            )
        }
        bind<SearchContract.ViewModel>() with provider { vm<SearchViewModel>(instance()) }
    }

    private val viewModel: SearchContract.ViewModel by instance()
    private lateinit var binding: ActivitySearchBindingImpl

    private val adapter: SearchAdapter by lazy {
        SearchAdapter (
            {
                viewModel.openGroupList(it)
            },
            {
                viewModel.onMMClick(it)
            },
            {
                viewModel.onGroupClick(it)
            }
        )
    }

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.adapter = this.adapter
        binding.model = viewModel


        val decorator = TopListDecorator(resources.getDimension(R.dimen.groups_list_top_padding).toInt())
        binding.rvSearch.addItemDecoration(decorator)

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }

        binding.tvSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.submitRequest(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}