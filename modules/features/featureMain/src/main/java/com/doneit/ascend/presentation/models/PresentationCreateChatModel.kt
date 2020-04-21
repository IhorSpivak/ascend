package com.doneit.ascend.presentation.models

class PresentationCreateChatModel {
    val title: ValidatableField = ValidatableField()
    var chatMembers: List<Long> = mutableListOf()
}