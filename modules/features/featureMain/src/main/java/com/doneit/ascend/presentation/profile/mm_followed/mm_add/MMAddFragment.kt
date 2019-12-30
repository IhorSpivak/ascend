package com.doneit.ascend.presentation.profile.mm_followed.mm_add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAddMasterMindBinding
import com.doneit.ascend.presentation.profile.mm_followed.common.FollowedAdapter
import org.kodein.di.generic.instance

class MMAddFragment : BaseFragment<FragmentAddMasterMindBinding>() {

    override val viewModelModule = MMAddViewModelModule.get(this)
    override val viewModel: MMAddContract.ViewModel by instance()

    private val adapter: FollowedAdapter by lazy {
        FollowedAdapter(follow = {
            viewModel.follow(it)
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        val decorator =
            TopListDecorator(resources.getDimension(R.dimen.search_list_top_padding).toInt())

        binding.model = viewModel
        binding.lifecycleOwner = this
        binding.content.adapter = adapter
        binding.content.addItemDecoration(decorator)

        viewModel.masterMinds.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

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