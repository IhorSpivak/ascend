package com.doneit.ascend.presentation.main.home.webinars

import android.os.Bundle
import android.widget.CheckBox
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.main.home.webinars.common.WebinarFilter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class WebinarsFragment : BaseFragment<FragmentWebinarsBinding>() {
    override val viewModelModule = WebinarsViewModelModule.get(this)
    override val viewModel: WebinarsContract.ViewModel by instance()

    private val userId: Long? by lazy {
        arguments?.getLong(USER_ID)
    }

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(
            null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id, it.groupType!!)
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
        }
        observeData()
        setListeners()
    }

    private fun setListeners() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            updateFilter()
        }
        for(child in radioContainer.children){
            (child as CheckBox).setOnCheckedChangeListener { _, _ ->
                updateFilter()
            }
        }
    }

    private fun observeData() {
        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.setUser(it.user)
            binding.radioContainer.children
                .withIndex()
                .filter { (it.value as CheckBox).isChecked }
                .joinToString {
                    WebinarFilter.values()[it.index].toString().toLowerCase()
                }.let { adapter.setCommunity(it) }
            adapter.submitList(it.groups)
        })
        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            viewModel.checkUser(user)
            WebinarFilter.values().firstOrNull {
                it.toString() == user.community!!.toLowerCase()
            }?.ordinal?.let {
                (binding.radioContainer.getChildAt(it) as CheckBox).isChecked = true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        updateFilter()
    }

    private fun updateFilter() {
        binding.radioContainer.children
            .withIndex()
            .filter { (it.value as CheckBox).isChecked }
            .joinToString(separator = ",") {
                WebinarFilter.values()[it.index].toString().toLowerCase()
            }.let { viewModel.updateFilter(it, userId) }
    }

    companion object {

        private const val USER_ID = "key_user_id"

        fun newInstance(userId: Long? = null) = WebinarsFragment().apply {
            arguments = Bundle().apply {
                userId?.let { putLong(USER_ID, it) }
            }
        }
    }
}