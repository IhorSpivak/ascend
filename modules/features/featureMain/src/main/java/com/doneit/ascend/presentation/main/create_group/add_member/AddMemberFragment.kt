package com.doneit.ascend.presentation.main.create_group.add_member

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AddMemberFragment : BaseFragment<FragmentAddMemberBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateMMGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: AddMemberContract.ViewModel by instance()

    private val memberAdapter: MemberAdapter by lazy {
        MemberAdapter(
            isPublic,
            {viewModel.onAdd(it) },
            {viewModel.onRemove(it)},
            {viewModel.onInviteClick(it)}
        )
    }
    private var isPublic: Boolean = true

    override fun viewCreated(savedInstanceState: Bundle?) {
        isPublic = arguments!!.getBoolean(IS_PUBLIC, true)
        binding.apply {
            lifecycleOwner = this@AddMemberFragment
            rvMembers.adapter = memberAdapter
            isEmpty = false
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
            }
            btnBack.setOnClickListener {
                viewModel.members.postValue(viewModel.selectedMembers)
                fragmentManager?.popBackStack()
            }
        }

        viewModel.searchResult.observe(this, Observer {
            memberAdapter.submitList(it)
            binding.apply {
                isEmpty = it.isEmpty()
                query = tvSearch.text.toString()
            }
        })

        binding.tvSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.onQueryTextChange(p0.toString())
                if (p0.isNullOrBlank()){
                    binding.apply {
                        isEmpty = false
                        rvMembers.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }



    companion object{
        private const val IS_PUBLIC = "isPublic"
        fun getInstance(individual: Boolean): AddMemberFragment{
            return AddMemberFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_PUBLIC, individual)
                }
            }
        }
    }
}