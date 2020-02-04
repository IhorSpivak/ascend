package com.doneit.ascend.presentation.utils

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.group.GroupStatus

fun getButtonType(user: UserEntity, group: GroupEntity): ButtonType {
    var res = ButtonType.SUBSCRIBE

    if (group.subscribed == true) {
        res = ButtonType.SUBSCRIBED
        if (group.status == GroupStatus.STARTED || group.status == GroupStatus.ACTIVE) {
            res = ButtonType.JOIN_TO_DISCUSSION
        }
    }

    if (user.isMasterMind && user.id == group.owner?.id) {
        res = if (group.status == GroupStatus.STARTED) {
            ButtonType.JOIN_TO_DISCUSSION
        } else if (group.status == GroupStatus.ACTIVE) {
            ButtonType.START_GROUP
        } else if (group.participantsCount == 0) {
            ButtonType.DELETE_GROUP
        } else {
            ButtonType.NONE
        }
    }

    return res
}