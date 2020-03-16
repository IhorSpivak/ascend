package com.doneit.ascend.presentation.main.create_group.add_member

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberListAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.main.databinding.FragmentCreateIndividualGroupBindingImpl
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
    val memberAdapter: MemberListAdapter by lazy {
        MemberListAdapter()
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this

        binding.rvMembers.adapter = memberAdapter

        memberAdapter.members = listOf("NAME1", "NAME2","NAME3")

        binding.btnBack.setOnClickListener {
            //viewModel.goBack()
        }

        binding.tvSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //viewModel.submitRequest(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    companion object{
        private const val INDIVIDUAL = "individual"
        fun getInstance(individual: Boolean): AddMemberFragment{
            return AddMemberFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(INDIVIDUAL, individual)
                }
            }
        }
    }
}