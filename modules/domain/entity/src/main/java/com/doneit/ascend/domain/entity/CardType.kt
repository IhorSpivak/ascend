package com.doneit.ascend.domain.entity

enum class CardType {
    VISA,
    MASTER_CARD;

    override fun toString(): String {
        return super.toString().toLowerCase().capitalize()
    }}