package com.doneit.ascend.presentation.profile.payments.my_transactions

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMyTransactionsBinding
import com.doneit.ascend.presentation.profile.payments.my_transactions.common.TransactionsAdapter
import kotlinx.android.synthetic.main.fragment_my_transactions.*
import org.kodein.di.generic.instance

class MyTransactionsFragment : BaseFragment<FragmentMyTransactionsBinding>() {

    override val viewModelModule = MyTransactionsViewModelModule.get(this)
    override val viewModel: MyTransactionsContract.ViewModel by instance()

    private val adapter: TransactionsAdapter by lazy { TransactionsAdapter() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.rvPayments.adapter = adapter
        val decorator =
            TopListDecorator(resources.getDimension(R.dimen.payments_list_top_padding).toInt())
        binding.rvPayments.addItemDecoration(decorator)

        viewModel.transtactions.observe(viewLifecycleOwner, Observer {
            srLayout.isRefreshing = false
            adapter.submitList(it)
        })

        srLayout.setOnRefreshListener {
            viewModel.refetch()
        }
    }
}