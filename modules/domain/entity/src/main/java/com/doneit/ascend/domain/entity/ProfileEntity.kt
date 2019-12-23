package com.doneit.ascend.domain.entity

class ProfileEntity(
    val id: Long,
    val fullName: String?,
    val email: String?,
    val phone: String?,
    val age: Int?,
    val location: String?,
    val community: String?,
    val following: Int?,
    val groupsCount: Int?,
    val image: String?
)