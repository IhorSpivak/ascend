package com.doneit.ascend.presentation.utils

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.group.GroupStatus

fun getButtonType(user: UserEntity, group: GroupEntity): ButtonType {
    var res = ButtonType.SUBSCRIBE

    if (group.subscribed == true) {
        res = ButtonType.SUBSCRIBED
        if (group.isStarting || group.inProgress) {
            res = ButtonType.JOIN_TO_DISCUSSION
        }
    }

    if (user.isMasterMind && user.id == group.owner?.id) {
        res = if (group.isStarting && group.status != GroupStatus.STARTED) {
            ButtonType.START_GROUP
        } else if (group.inProgress) {
            ButtonType.JOIN_TO_DISCUSSION
        } else if (group.participantsCount == 0) {
            ButtonType.DELETE_GROUP
        } else {
            ButtonType.NONE
        }
    }

    return res
}