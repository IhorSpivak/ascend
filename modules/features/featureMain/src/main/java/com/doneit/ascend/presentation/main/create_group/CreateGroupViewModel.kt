package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.main.models.ValidatableField
import com.doneit.ascend.presentation.main.models.ValidationResult
import com.doneit.ascend.presentation.main.models.toEntity
import com.doneit.ascend.presentation.utils.*
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class CreateGroupViewModel(
    private val groupUseCase: GroupUseCase,
    private val router: CreateGroupRouter
) : BaseViewModelImpl(), CreateGroupContract.ViewModel {

    override val createGroupModel = PresentationCreateGroupModel()
    override var email: ValidatableField = ValidatableField()
    override val canCreate = MutableLiveData<Boolean>()
    override val canAddParticipant = MutableLiveData<Boolean>()

    override val participants = MutableLiveData<List<String>>()

    init {
        createGroupModel.name.validator = { s ->
            val result = ValidationResult()

            if (s.isValidGroupName().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_group_name)
            }

            result
        }

        createGroupModel.numberOfMeetings.validator = { s ->
            val result = ValidationResult()

            if (s.isValid4Number().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_number_of_meetings)
            }

            result
        }

        createGroupModel.startDate.validator = { s ->
            val result = ValidationResult()

            if (s.isValidStartDate().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_start_date)
            }

            result
        }

        createGroupModel.price.validator = { s ->
            val result = ValidationResult()

            if (s.isValid4Number().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_price)
            }

            result
        }

        createGroupModel.description.validator = { s ->
            val result = ValidationResult()

            if (s.isDescriptionValid().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_description)
            }

            result
        }

        email.validator = { s ->
            val result = ValidationResult()

            if (s.isValidEmail().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_email)
            }

            result
        }

        val invalidationListener = { updateCanCreate() }
        createGroupModel.name.onFieldInvalidate = invalidationListener
        // TODO: uncommite when calendar picker is ready
//        createGroupModel.schedule.onFieldInvalidate = invalidationListener
        createGroupModel.numberOfMeetings.onFieldInvalidate = invalidationListener
        createGroupModel.startDate.onFieldInvalidate = invalidationListener
        createGroupModel.price.onFieldInvalidate = invalidationListener
        createGroupModel.description.onFieldInvalidate = invalidationListener

        email.onFieldInvalidate = { updateCanAddParticipant() }
    }

    override fun addNewParticipant() {
        val newParticipantEmail = email.observableField.get()

        newParticipantEmail?.let {

            val participantsList = participants.value
            val newParticipantList = mutableListOf<String>()

            if (participantsList != null && participantsList.isNotEmpty()) {
                newParticipantList.addAll(participantsList)
            }

            newParticipantList.add(newParticipantEmail)
            participants.postValue(newParticipantList)
            createGroupModel.participants.set(newParticipantList)

            email.observableField.set("")
        }
    }

    override fun completeClick() {
        canCreate.postValue(false)

        viewModelScope.launch {
            val requestEntity =
                groupUseCase.createGroup(createGroupModel.toEntity("master_mind")) // TODO: fix group type

            canCreate.postValue(true)

            if (requestEntity.isSuccessful) {
                router.onBack()
            }
        }
    }

    override fun backClick() {
        router.onBack()
    }

    override fun onClickRemove(value: String) {
        val participantsList = participants.value
        val newParticipantList = mutableListOf<String>()

        if (participantsList != null && participantsList.isNotEmpty()) {
            newParticipantList.addAll(participantsList.filter { s -> s != value })
        }

        participants.postValue(newParticipantList)
        createGroupModel.participants.set(newParticipantList)
    }

    private fun updateCanCreate() {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        // TODO: uncommite when calendar picker is ready
//        isFormValid = isFormValid and createGroupModel.schedule.isValid
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
        isFormValid = isFormValid and createGroupModel.price.isValid
        isFormValid = isFormValid and createGroupModel.description.isValid

        canCreate.postValue(isFormValid)
    }

    private fun updateCanAddParticipant() {
        var isFormValid = true

        isFormValid = isFormValid and email.isValid

        canAddParticipant.postValue(isFormValid)
    }
}