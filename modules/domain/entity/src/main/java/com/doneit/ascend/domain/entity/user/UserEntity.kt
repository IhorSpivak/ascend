package com.doneit.ascend.domain.entity.user

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
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
    val registrationType: RegistrationType,
    val unansweredQuestionsCount: Int,
    val unreadNotificationsCount: Int,
    var blockedUsersCount: Int,
    val image: ImageEntity?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float?,
    val rated: Boolean,
    val myRating: Int?,
    val followed: Boolean,
    val allowRating: Boolean,
    val groupsCount: Int,
    val isMasterMind: Boolean,
    val community: String?,
    val visitedGroupCount: Int,
    val birthday: Date?,
    val communities: List<String>?
) {
    val age: Int?
        get() {
            return if (birthday == null) null else getAge(
                birthday
            )
        }

    companion object {

        fun getAge(birthDate: Date): Int {
            val currentDate = getDefaultCalendar()
            val birthday = getDefaultCalendar()
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