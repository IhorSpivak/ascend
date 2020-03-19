package com.doneit.ascend.presentation.utils

enum class GroupAction {
    DUPLICATE,
    EDIT;
    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}