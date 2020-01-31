package com.doneit.ascend.presentation.main.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentSearchBinding
import com.doneit.ascend.presentation.main.search.common.SearchAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val viewModelModule = SearchViewModelModule.get(this)
    override val viewModel: SearchContract.ViewModel by instance()

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
            },
            {
                if(it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            }
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
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