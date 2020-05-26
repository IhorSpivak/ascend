package com.doneit.ascend.domain.entity.dto


sealed class GroupCredentialsDTO {
    data class TwilioCredentialsDTO(val name: String, val token: String): GroupCredentialsDTO()
    data class VimeoCredentialsDTO(val chatId: String, val link: String, val key: String): GroupCredentialsDTO()
}