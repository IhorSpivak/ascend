package com.doneit.ascend.domain.entity.dto

enum class PaymentType {
    CARD,
    BANK_ACCOUNT;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}