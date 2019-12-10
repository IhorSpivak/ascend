package com.doneit.ascend.domain.entity.dto

enum class SortType {
    ASC,
    DESC;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }}