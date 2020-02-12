package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.ChangeEmailModel
import com.doneit.ascend.domain.entity.dto.ChangePasswordModel
import com.doneit.ascend.domain.entity.dto.ChangePhoneModel
import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.utils.getNotNull
import com.stripe.android.model.Card
import java.lang.NumberFormatException
import java.util.*

fun PresentationCreateGroupModel.toEntity(): CreateGroupModel {
    val startTime =
        CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())
    val calendar = getDefaultCalendar()
    calendar.time = startTime!!
    calendar.set(Calendar.HOUR, hours.toInt() )//% 12to avoid day increment
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.AM_PM, timeType.toAM_PM())

    return CreateGroupModel(
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

fun EditPhoneModel.toEntity(): ChangePhoneModel {
    return ChangePhoneModel(
        password.observableField.getNotNull(),
        getPhone(),
        code.observableField.getNotNull()
    )
}

fun PresentationChangePasswordModel.toEntity(): ChangePasswordModel {
    return ChangePasswordModel(
        currentPassword.observableField.getNotNull(),
        newPassword.observableField.getNotNull(),
        confirmPassword.observableField.getNotNull()
    )
}

fun EditEmailModel.toEntity(): ChangeEmailModel {
    return ChangeEmailModel(
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
        userId,
        fullName,
        image,
        isHandRisen
    )
}

fun ParticipantEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        id,
        fullName,
        image,
        isHandRisen,
        isMuted
    )
}