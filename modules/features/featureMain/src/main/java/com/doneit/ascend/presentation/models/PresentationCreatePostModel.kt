package com.doneit.ascend.presentation.models

class PresentationCreatePostModel {
    var header: ValidatableField = ValidatableField()
    var description: ValidatableField = ValidatableField()
    var mediaList: List<String>? = listOf()
}