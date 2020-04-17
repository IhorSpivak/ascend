package com.doneit.ascend.presentation.main.home.webinars

import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.main.home.webinars.common.WebinarFilter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class WebinarsFragment : BaseFragment<FragmentWebinarsBinding>(){
    override val viewModelModule = WebinarsViewModelModule.get(this)
    override val viewModel: WebinarsContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(
            null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            },
            null
        )
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            rvGroups.adapter = adapter
            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                radioGroup.children.forEach {
                    if (it.id == i) {
                        (it as RadioButton).let {
                            viewModel.updateFilter(WebinarFilter.values()[radioGroup.indexOfChild(it)])
                        }
                    }
                }
            }
        }
        viewModel.groups.observe(this, Observer {
            adapter.setUser(it.user)
            binding.radioGroup.children.indexOfFirst { (it as RadioButton).isChecked }?.let {
                adapter.setCommunity(WebinarFilter.values()[it].toString().capitalize())
            }
            adapter.submitList(it.groups)
        })
        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        binding.swipeRefresh.setOnRefreshListener {
            binding.radioGroup.children.indexOfFirst { (it as RadioButton).isChecked }?.let {
                viewModel.updateFilter(WebinarFilter.values()[it])
            }
        }
        viewModel.userLiveData.observe(this, Observer { user ->
            viewModel.checkUser(user)
            WebinarFilter.values().firstOrNull {
                it.toString() == user.community!!.toLowerCase()
            }?.ordinal?.let {
                binding.radioGroup.apply {
                    check(this[it].id)
                }
            }
        })
    }
}