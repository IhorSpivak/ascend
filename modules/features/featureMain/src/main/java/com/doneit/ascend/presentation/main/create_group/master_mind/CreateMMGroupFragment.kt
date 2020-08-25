package com.doneit.ascend.presentation.main.create_group.master_mind

import android.graphics.Bitmap
import android.os.Bundle
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.individual.IndividualGroupFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateMmGroupBinding
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.copyToStorage
import com.doneit.ascend.presentation.utils.extensions.*
import com.doneit.ascend.presentation.utils.getNotNull
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
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
            if (arguments!!.containsKey(it.toString())) {
                group = arguments!!.getParcelable(it.toString())
                if (group != null) {
                    what = it.toString()
                }
            }
        }
        binding.apply {
            actionTitle = if (what == null) {
                getString(R.string.create_create)
            } else {
                what!!.capitalize()
            }
        }
        binding.mainContainer.setOnFocusChangeListener { v, b ->
            if (b) {
                hideKeyboard()
            }
        }
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })
        viewModel.members.observe(this, Observer {
            viewModel.createGroupModel.participants.set(it.filter { attendee ->
                !attendee.isAttended
            }.map { attendee ->
                attendee.email.orEmpty()
            })
        })
        viewModel.membersToDelete.observe(this, Observer {
            viewModel.createGroupModel.participantsToDelete.set(it.map { attendee ->
                attendee.email.orEmpty()
            })
        })

        viewModel.createGroupModel.isPrivate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.createGroupModel.isPrivate.getNotNull()) {
                    viewModel.onIndividualSelected()
                } else {
                    viewModel.onGroupSelected()
                }
            }
        })
        if (group != null) {
            binding.btbComplete.apply {
                text = getString(R.string.btn_save_action)
                setOnClickListener {
                    when (what) {
                        GroupAction.DUPLICATE.toString() -> viewModel.completeClick()
                        GroupAction.EDIT.toString() -> viewModel.updateGroup(group!!)
                    }
                }
            }
            when (group!!.groupType) {
                GroupType.INDIVIDUAL -> {
                    viewModel.createGroupModel.isPrivate.set(true)
                }
                GroupType.MASTER_MIND -> {
                    viewModel.createGroupModel.isPrivate.set(false)
                }
            }
            viewModel.createGroupModel.apply {
                when (what) {
                    GroupAction.DUPLICATE.toString() -> {
                        name.observableField.set(group!!.name.plus("(2)"))
                    }
                    GroupAction.EDIT.toString() -> {
                        name.observableField.set(group!!.name)
                    }
                }
                numberOfMeetings.observableField.set(group!!.meetingsCount.toString())
                price.observableField.set(group!!.price.toString())
                description.observableField.set(group!!.description)
                val date = group!!.startTime
                year = date!!.toYear()
                month = MonthEntity.values()[date!!.toMonth()]
                day = date!!.toDayOfMonth()
                hours = date!!.toCalendar().get(Calendar.HOUR).toTimeString()
                hoursOfDay = date!!.toCalendar().get(Calendar.HOUR_OF_DAY).toTimeString()
                minutes = date!!.toCalendar().get(Calendar.MINUTE).toTimeString()
                timeType = date!!.toCalendar().get(Calendar.AM_PM).toAmPm()
                groupType = GroupType.values()[group!!.groupType!!.ordinal]
                meetingFormat.observableField.set(group!!.meetingFormat.orEmpty())
                startDate.observableField.set(date.toDayFullMonthYear())
                selectedDays.addAll(group!!.daysOfWeek!!)
                duration.observableField.set(group!!.duration.toString())
                viewModel.changeSchedule()
                image.observableField.set(null)
                Glide.with(context!!)
                    .asBitmap()
                    .load(group!!.image!!.url)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            image.observableField.set(context?.copyToStorage(resource))
                        }

                    })
            }
            viewModel.apply {
                members.postValue(group!!.attendees?.toMutableList())
                selectedMembers.addAll(group!!.attendees ?: emptyList())
            }
        } else {
            binding.btbComplete.setOnClickListener {
                viewModel.completeClick()
            }
        }
    }

    private fun handleNavigation(action: CreateMMGroupContract.Navigation) {
        val fragment = when (action) {
            CreateMMGroupContract.Navigation.TO_GROUP -> {
                viewModel.createGroupModel.groupType = GroupType.MASTER_MIND
                CreateGroupFragment().apply {
                    arguments = Bundle().apply {
                        if (group != null) {
                            putParcelable(what, group)
                        }
                    }
                }
            }
            CreateMMGroupContract.Navigation.TO_INDIVIDUAL -> {
                viewModel.createGroupModel.groupType = GroupType.INDIVIDUAL
                IndividualGroupFragment(group)
            }
        }
        viewModel.apply {
            selectedMembers.clear()
            members.postValue(selectedMembers)
        }

        childFragmentManager.replace(R.id.container, fragment)
    }
}