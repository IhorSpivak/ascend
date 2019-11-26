package com.doneit.ascend.presentation.login.models

class PresentationAnswerModel(
    var questionId: Long,
    var answerOptionId: Long,
    val answer: ValidatableField = ValidatableField()
)