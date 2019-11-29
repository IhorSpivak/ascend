package com.doneit.ascend.domain.entity

data class UserEntity(
   val name: String?,
   val email: String?,
   val phone: String?,
   val createdAt: String?,
   val updatedAt: String?,
   val unansweredQuestions: List<Int>?
)