package com.doneit.ascend.presentation.utils

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

fun getButonType(user: UserEntity, group: GroupEntity): ButtonType {
    val states = mutableListOf(false, false, false, false)

    var res = ButtonType.SUBSCRIBE

    if (group.subscribed == true) {
        res = ButtonType.JOINED
        if (group.inProgress) {
            res = ButtonType.JOIN_TO_DISCUSSION
        }
    }

    if (user.isMasterMind && user.id == group.owner?.id) {
        if (group.isStarting) {
            res = ButtonType.START_GROUP
        } else if (group.participantsCount == 0) {
            res = ButtonType.DELETE_GROUP
        }
    }

    return res
}

fun String.getCardNumberType() : CardAssociation {
    val listOfPattern = getCardNumberPattern()
    val ccNum = this.replace("\\s".toRegex(), "")
    for ((index, element) in listOfPattern.withIndex()) {
        if (ccNum.matches(element.toRegex())) {
            when(index){
                0 -> return CardAssociation.VISA
                1 -> return CardAssociation.MASTERCARD
            }
        }
    }
    return CardAssociation.INVALID
}

private fun getCardNumberPattern() : ArrayList<String>{
    val listOfPattern = ArrayList<String>()
    val ptVisa = "^4[0-9]{3,}$"
    listOfPattern.add(ptVisa)
    val ptMasterCard = "^5[1-5][0-9]{2,}$"
    listOfPattern.add(ptMasterCard)
    val ptAmeExp = "^3[47][0-9]{2,}$"
    listOfPattern.add(ptAmeExp)
    val ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{1,}$"
    listOfPattern.add(ptDinClb)
    val ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{0,}$"
    listOfPattern.add(ptDiscover)
    val ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{0,}$"
    listOfPattern.add(ptJcb)
    return listOfPattern
}