package com.doneit.ascend.presentation.utils

enum class CardAssociation {
    INVALID,
    VISA,
    MASTERCARD;

    override fun toString(): String {
        var res = super.toString()
        res = res.toLowerCase()
        res = res.capitalize()
        return res
    }}