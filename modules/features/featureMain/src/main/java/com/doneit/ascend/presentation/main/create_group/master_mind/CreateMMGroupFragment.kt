package com.doneit.ascend.presentation.main.create_group.master_mind

import android.os.Bundle
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateMmGroupBinding
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.utils.getNotNull
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CreateMMGroupFragment : ArgumentedFragment<FragmentCreateMmGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateMMGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: CreateMMGroupContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })

        viewModel.createGroupModel.isPublic.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.createGroupModel.isPublic.getNotNull()) {
                    viewModel.onGroupSelected()
                } else {
                    viewModel.onIndividualSelected()
                }
            }
        })
    }

    private fun handleNavigation(action: CreateMMGroupContract.Navigation) {
        val fragment = when (action) {
            CreateMMGroupContract.Navigation.TO_GROUP -> {
                CreateGroupFragment()
            }
            CreateMMGroupContract.Navigation.TO_INDIVIDUAL -> {
                IndividualGroupFragment()
            }
        }

        childFragmentManager.replace(R.id.container, fragment)
    }
}