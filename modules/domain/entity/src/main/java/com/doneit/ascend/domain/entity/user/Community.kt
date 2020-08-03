package com.doneit.ascend.domain.entity.user

enum class Community(
    val title: String
) {
    RECOVERY("Recovery"),
    LIFESTYLE("Lifestyle"),
    INDUSTRY("Industry"),
    FAMILY("Family"),
    SUCCESS("Success"),
    SPIRITUAL("Spiritual");


    override fun toString(): String {
        return title
    }

}