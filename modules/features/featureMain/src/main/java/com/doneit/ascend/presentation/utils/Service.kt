package com.doneit.ascend.presentation.utils

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

fun getButonType(user: UserEntity, group: GroupEntity): ButtonType {
    var res = ButtonType.SUBSCRIBE

    if (group.subscribed == true) {
        res = ButtonType.SUBSCRIBED
        if (group.inProgress || group.isStarting) {
            res = ButtonType.JOIN_TO_DISCUSSION
        }
    }

    if (user.isMasterMind && user.id == group.owner?.id) {
        res = if (group.inProgress) {
            ButtonType.JOIN_TO_DISCUSSION
        } else if (group.isStarting) {
            ButtonType.START_GROUP
        } else if (group.participantsCount == 0) {
            ButtonType.DELETE_GROUP
        } else {
            ButtonType.NONE
        }
    }

    if(group.blocked == true) {
        res = ButtonType.NONE
    }

    return res
}