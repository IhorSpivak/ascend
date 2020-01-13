package com.doneit.ascend.presentation.utils


fun String.isValidCardNumber() : Boolean {
    val cardFormatted =  this.replace("\\s".toRegex(), "")
    val r = Regex("^[0-9]{16}\$")
    return cardFormatted.matches(r)
}

fun String.isValidCVV() : Boolean {
    val cardFormatted =  this.replace("\\s".toRegex(), "")
    val r = Regex("^[0-9]{3}\$")
    return this.matches(r)
}

fun String.isValidExpiration() : Boolean {
    val cardFormatted =  this.replace("\\s".toRegex(), "")
    val r = Regex("^(0[1-9]|1[0-2])/[0-9]{2}$")
    return this.matches(r)
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