package com.doneit.ascend.domain.entity.user

enum class RegistrationType {
    FACEBOOK,
    GOOGLE,
    TWITTER,
    REGULAR;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    companion object{
        fun fromRemoteString(type: String?): RegistrationType {
            return values().firstOrNull { it.toString() == type } ?: REGULAR
        }
    }
}