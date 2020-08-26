package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.presentation.models.group.ParticipantSourcePriority
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.models.group.WebinarChatParticipant
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.getNotNull
import com.stripe.android.model.Card
import com.twilio.video.LocalParticipant
import com.twilio.video.RemoteParticipant
import java.text.SimpleDateFormat
import java.util.*

fun EditPhoneModel.toEntity(): ChangePhoneDTO {
    return ChangePhoneDTO(
        password = password.observableField.getNotNull(),
        phoneNumber = getPhone(),
        code = code.observableField.getNotNull()
    )
}

fun PresentationChangePasswordModel.toEntity(): ChangePasswordDTO {
    return ChangePasswordDTO(
        currentPassword = currentPassword.observableField.getNotNull(),
        password = newPassword.observableField.getNotNull(),
        passwordConfirmation = confirmPassword.observableField.getNotNull()
    )
}

fun EditEmailModel.toEntity(): ChangeEmailDTO {
    return ChangeEmailDTO(
        email = email.observableField.getNotNull(),
        password = password.observableField.getNotNull()
    )
}

fun CardEntity.toPresentation(): PresentationCardModel {
    return PresentationCardModel(
        id = id,
        name = name,
        brand = brand,
        country = country,
        expMonth = expMonth,
        expYear = expYear,
        last4 = last4,
        cratedAt = createdAt,
        updatedAt = updatedAt,
        isSelected = isDefault
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
        source = ParticipantSourcePriority.SOCKET,
        userId = userId.toString(),
        fullName = fullName,
        image = image,
        isHandRisen = isHandRisen
    )
}

fun SocketUserEntity.toWebinarPresentation(): WebinarChatParticipant {
    return WebinarChatParticipant(
        userId = userId.toString(),
        fullName = fullName,
        image = image,
        isConnected = true
    )
}

fun ParticipantEntity.toWebinarPresentation(): WebinarChatParticipant {
    return WebinarChatParticipant(
        userId = id.toString(),
        fullName = fullName,
        image = image,
        isConnected = isConnected
    )
}

fun ParticipantEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        source = ParticipantSourcePriority.REQUEST,
        userId = id.toString(),
        fullName = fullName,
        image = image,
        isSpeaker = isSpeaker,
        isHandRisen = isHandRisen,
        isMuted = isMuted
    )
}

fun RemoteParticipant.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        source = ParticipantSourcePriority.TWILIO,
        userId = identity,
        remoteParticipant = this
    )
}

fun LocalParticipant.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        source = ParticipantSourcePriority.TWILIO,
        userId = identity,
        localParticipant = this,
        isOwner = true
    )
}

fun OwnerEntity.toPresentation(): PresentationChatParticipant {
    return PresentationChatParticipant(
        source = ParticipantSourcePriority.REQUEST,
        userId = id.toString(),
        fullName = fullName,
        isOwner = true
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

fun MessageSocketEntity.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        message = message ?: "",
        edited = edited ?: false,
        type = type?.toMessageType() ?: MessageType.MESSAGE,
        userId = userId ?: 0,
        createdAt = createdAt!!.toDate(),
        updatedAt = updatedAt!!.toDate(),
        status = status!!.toMessageStatus(),
        post = post,
        attachment = attachment,
        sharedGroup = sharedGroup,
        sharedUser = sharedUser
    )
}

fun QuestionSocketEntity.toEntity(): WebinarQuestionEntity {
    return WebinarQuestionEntity(
        id = id,
        content = question,
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
        userId = userId,
        fullName = fullName,
        image = image
    )
}

fun PresentationCreateChannelModel.toEntity(): NewChannelDTO {
    return NewChannelDTO(
        title = title.observableField.getNotNull(),
        description = description.observableField.get(),
        image = image.observableField.get(),
        isPrivate = isPrivate.getNotNull(),
        invites = participants.get().orEmpty()
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

fun String.toDate(): Date {
    return Constants.REMOTE_DATE_FORMAT_FULL.toDefaultFormatter().parse(this) ?: Date()
}

private fun String.toDefaultFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun String.toMessageType(): MessageType {
    return when(this) {
        MessageType.INVITE.toString() -> MessageType.INVITE
        MessageType.LEAVE.toString() -> MessageType.LEAVE
        MessageType.USER_REMOVED.toString() -> MessageType.USER_REMOVED
        MessageType.ATTACHMENT.toString() -> MessageType.ATTACHMENT
        MessageType.POST_SHARE.toString() -> MessageType.POST_SHARE
        MessageType.GROUP_SHARE.toString() -> MessageType.GROUP_SHARE
        MessageType.PROFILE_SHARE.toString() -> MessageType.PROFILE_SHARE
        else -> MessageType.MESSAGE
    }
}