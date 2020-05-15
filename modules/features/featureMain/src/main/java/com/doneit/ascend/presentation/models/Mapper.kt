package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketUserEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.dto.ChangeEmailDTO
import com.doneit.ascend.domain.entity.dto.ChangePasswordDTO
import com.doneit.ascend.domain.entity.dto.ChangePhoneDTO
import com.doneit.ascend.domain.entity.dto.CreateAttachmentDTO
import com.doneit.ascend.presentation.models.group.ParticipantSourcePriority
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.getNotNull
import com.stripe.android.model.Card
import com.twilio.video.RemoteParticipant
import java.text.SimpleDateFormat
import java.util.*

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
        ParticipantSourcePriority.SOCKET,
        userId.toString(),
        fullName,
        image,
        isHandRisen
    )
}

fun ParticipantEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        ParticipantSourcePriority.REQUEST,
        id.toString(),
        fullName,
        image,
        isSpeaker = isSpeaker,
        isHandRisen = isHandRisen,
        isMuted = isMuted
    )
}

fun RemoteParticipant.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        ParticipantSourcePriority.TWILIO,
        userId = identity,
        remoteParticipant = this
    )
}

fun CreateAttachmentModel.toEntity(): CreateAttachmentDTO {
    return CreateAttachmentDTO(
        groupId = groupId,
        attachmentType = attachmentType,
        fileName = name.observableField.getNotNull(),
        link = link.observableField.getNotNull(),
        private = isPrivate
    )
}

fun CreateAttachmentFileModel.toEntity(): CreateAttachmentDTO {
    return CreateAttachmentDTO(
        groupId = groupId,
        attachmentType = attachmentType,
        fileName = name,
        link = link.orEmpty(),
        fileSize = size,
        private = isPrivate
    )
}

fun MessageSocketEntity.toEntity(): MessageEntity{
    return MessageEntity(
        id,
        message?:"",
        edited?: false,
        type?.toMessageType()?: MessageType.MESSAGE,
        userId?:0,
        createdAt!!.toDate(),
        updatedAt!!.toDate(),
        status!!.toMessageStatus()
    )
}

fun String.toMessageStatus(): MessageStatus {
    return when (this) {
        "sent" -> MessageStatus.SENT
        "delivered" -> MessageStatus.DELIVERED
        "read" -> MessageStatus.READ
        else -> MessageStatus.ALL
    }
}

fun String.toDate(): Date? {
    return Constants.REMOTE_DATE_FORMAT_FULL.toDefaultFormatter().parse(this)
}

fun String.toDefaultFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun String.toMessageType(): MessageType{
    return when{
        this == MessageType.INVITE.toString() -> MessageType.INVITE
        this == MessageType.LEAVE.toString() -> MessageType.LEAVE
        else -> MessageType.MESSAGE
    }
}