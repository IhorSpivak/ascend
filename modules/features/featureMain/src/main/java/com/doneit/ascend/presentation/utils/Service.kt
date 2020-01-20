package com.doneit.ascend.presentation.utils

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

fun getButonType(user: UserEntity, group: GroupEntity): ButtonType {
    var res = ButtonType.SUBSCRIBE

    if (group.subscribed == true) {
        res = ButtonType.JOINED
        if (group.inProgress || group.isStarting) {
            res = ButtonType.JOIN_TO_DISCUSSION
        }
    }

    if (user.isMasterMind && user.id == group.owner?.id) {
        if (group.isStarting || group.inProgress) {
            res = ButtonType.START_GROUP
        } else if (group.participantsCount == 0) {
            res = ButtonType.DELETE_GROUP
        } else {
            res = ButtonType.NONE
        }
    }

    return res
}