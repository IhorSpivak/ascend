package com.doneit.ascend.presentation.main.create_group.master_mind

import android.os.Bundle
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateMmGroupBinding
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.extensions.*
import com.doneit.ascend.presentation.utils.getNotNull
import kotlinx.android.synthetic.main.fragment_create_individual_group.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.text.SimpleDateFormat
import java.util.*

class CreateMMGroupFragment : ArgumentedFragment<FragmentCreateMmGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateMMGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: CreateMMGroupContract.ViewModel by instance()
    private var group: GroupEntity? = null
    private var what: String? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        GroupAction.values().forEach {
            if (arguments!!.containsKey(it.toString())){
                group = arguments!!.getParcelable(it.toString())
                if (group != null){
                    what = it.toString()
                }
            }
        }
        binding.apply {
            actionTitle = if (what == null){
                getString(R.string.create_create)
            }else{
                what!!.capitalize()
            }
        }
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })
        viewModel.members.observe(this, Observer {
            viewModel.createGroupModel.participants.set(it.map {
                it.email
            })
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
        if (group != null){
            binding.btbComplete.apply {
                text = getString(R.string.btn_save_action)
                setOnClickListener {
                    when (what) {
                        GroupAction.DUPLICATE.toString() -> viewModel.completeClick()
                        GroupAction.EDIT.toString() -> viewModel.updateGroup(group!!.id)
                    }
                }
            }
            when(group!!.groupType){
                GroupType.INDIVIDUAL ->{
                    viewModel.createGroupModel.isPublic.set(false)
                }
                GroupType.MASTER_MIND -> {
                    viewModel.createGroupModel.isPublic.set(true)
                }
            }
            viewModel.createGroupModel.apply {
                when(what){
                    GroupAction.DUPLICATE.toString() ->{name.observableField.set(group!!.name.plus("(2)"))}
                    GroupAction.EDIT.toString() ->{name.observableField.set(group!!.name)}
                }
                numberOfMeetings.observableField.set(group!!.meetingsCount.toString())
                price.observableField.set(group!!.price.toString())
                description.observableField.set(group!!.description)
                val date = group!!.startTime
                year = date!!.toYear()
                month = MonthEntity.values()[date!!.toMonth()]
                day = date!!.toDayOfMonth()
                hours = date!!.toCalendar().get(Calendar.HOUR_OF_DAY).toTimeString()
                minutes = date!!.toCalendar().get(Calendar.MINUTE).toTimeString()
                timeType = date!!.toCalendar().get(Calendar.AM_PM).toAmPm()
                groupType = com.doneit.ascend.presentation.models.GroupType.values()[group!!.groupType!!.ordinal]
                meetingFormat.observableField.set(group!!.meetingFormat?: "")
                startDate.observableField.set(SimpleDateFormat("dd MMMM yyyy").format(date))
                selectedDays.addAll(group!!.daysOfWeek!!)
                viewModel.changeSchedule()
                image.observableField.set(group!!.image!!.url)
            }
            viewModel.apply{
                members.postValue(group!!.attendees?.toMutableList())
                selectedMembers.addAll(group!!.attendees?: emptyList())
            }
        }else{
            binding.btbComplete.setOnClickListener {
                viewModel.completeClick()
            }
        }
    }

    private fun handleNavigation(action: CreateMMGroupContract.Navigation) {
        val fragment = when (action) {
            CreateMMGroupContract.Navigation.TO_GROUP -> {
                viewModel.createGroupModel.groupType = com.doneit.ascend.presentation.models.GroupType.MASTER_MIND
                CreateGroupFragment()
            }
            CreateMMGroupContract.Navigation.TO_INDIVIDUAL -> {
                viewModel.apply {
                    if (selectedMembers.size > 1){
                        val temp = selectedMembers.first()
                        selectedMembers.clear()
                        selectedMembers.add(temp)
                        members.postValue(selectedMembers)
                    }
                }
                viewModel.createGroupModel.groupType = com.doneit.ascend.presentation.models.GroupType.INDIVIDUAL
                IndividualGroupFragment()
            }
        }

        childFragmentManager.replace(R.id.container, fragment)
    }
}