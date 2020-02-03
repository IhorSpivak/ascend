package com.doneit.ascend.domain.entity

import java.util.*

data class UserEntity(
    val id: Long,
    val fullName: String?,
    val email: String?,
    val phone: String?,
    val location: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val meetingStarted: Boolean?,
    val newGroups: Boolean?,
    val inviteToMeeting: Boolean?,
    val unansweredQuestions: List<Int>?,
    val image: ImageEntity?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float?,
    val role: String?,
    val community: String?,
    val visitedGroupCount: Int,
    val birthday: Date?
) {
    val age: Int?
        get() {
            return if (birthday == null) null else getAge(birthday)
        }

    val isMasterMind: Boolean
        get() {
            return role == MM_ROLE
        }

    companion object {
        private const val MM_ROLE = "master_mind"

        fun getAge(birthDate: Date): Int {
            val currentDate = Calendar.getInstance()
            val birthday = Calendar.getInstance()
            birthday.time = birthDate

            var res = currentDate.get(Calendar.YEAR) - birthday.get(Calendar.YEAR)
            if (currentDate.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)
                || currentDate.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)
            ) {
                res = res!! - 1
            }

            return res
        }
    }
}