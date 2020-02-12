package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.ChangeEmailDTO
import com.doneit.ascend.domain.entity.dto.ChangePasswordDTO
import com.doneit.ascend.domain.entity.dto.ChangePhoneDTO
import com.doneit.ascend.domain.entity.dto.CreateGroupDTO
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.doneit.ascend.presentation.utils.getNotNull
import com.stripe.android.model.Card
import com.twilio.video.RemoteParticipant
import java.lang.NumberFormatException
import java.util.*

fun PresentationCreateGroupModel.toEntity(): CreateGroupDTO {
    val startTime =
        CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())
    val calendar = getDefaultCalendar()
    calendar.time = startTime!!
    calendar.set(Calendar.HOUR, hours.toInt() )//% 12to avoid day increment
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.AM_PM, timeType.toAM_PM())

    return CreateGroupDTO(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        calendar.time,
        groupType?.toString() ?: "",
        price.observableField.get()?.toFloatS(),
        image.observableField.getNotNull(),
        participants.get(),
        scheduleDays.toDays(),
        Integer.parseInt(numberOfMeetings.observableField.getNotNull()),
        meetingFormat.observableField.get(),
        isPublic.get(),
        tags.observableField.get()
    )
}

fun String.toFloatS(): Float? {
    var res: Float? = null

    try {
        res = this.toFloat()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }

    return res
}

fun List<CalendarDayEntity>.toDays(): List<Int> {
    return this.map {
        it.ordinal
    }
}

fun String.toAM_PM(): Int {
    return if (this == "AM") Calendar.AM else Calendar.PM
}

fun EditPhoneModel.toEntity(): ChangePhoneDTO {
    return ChangePhoneDTO(
        password.observableField.getNotNull(),
        getPhone(),
        code.observableField.getNotNull()
    )
}

fun PresentationChangePasswordModel.toEntity(): ChangePasswordDTO {
    return ChangePasswordDTO(
        currentPassword.observableField.getNotNull(),
        newPassword.observableField.getNotNull(),
        confirmPassword.observableField.getNotNull()
    )
}

fun EditEmailModel.toEntity(): ChangeEmailDTO {
    return ChangeEmailDTO(
        email.observableField.getNotNull(),
        password.observableField.getNotNull()
    )
}

fun CardEntity.toPresentation(): PresentationCardModel {
    return PresentationCardModel(
        id,
        name,
        brand,
        country,
        expMonth,
        expYear,
        last4,
        createdAt,
        updatedAt,
        isDefault
    )
}

fun PresentationCreateCardModel.toStripeCard(): Card {
    val number = cardNumber.observableField.getNotNull()
    val exp = expiration.observableField.getNotNull().split('/')
    val cvv = cvv.observableField.getNotNull()

    return Card.Builder(number, exp[0].toInt(), exp[1].toInt(), cvv).build()

}

fun String.toPresentationCommunity(isSelected: Boolean): PresentationCommunityModel {
    return PresentationCommunityModel(
        this,
        isSelected
    )
}

fun SocketUserEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        userId.toString(),
        fullName,
        image,
        isHandRisen
    )
}

fun ParticipantEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        id.toString(),
        fullName,
        image,
        isHandRisen,
        isMuted
    )
}

fun RemoteParticipant.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        userId = identity,
        remoteParticipant = this
    )
}