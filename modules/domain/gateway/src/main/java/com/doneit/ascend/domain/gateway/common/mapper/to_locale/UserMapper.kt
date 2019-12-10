package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.source.storage.local.data.UserLocal

fun UserEntity.toUserLocal(): UserLocal {
    return UserLocal(
        id = id,
        name = this@toUserLocal.name,
        email = this@toUserLocal.email,
        phone = this@toUserLocal.phone,
        createdAt = this@toUserLocal.createdAt,
        updatedAt = this@toUserLocal.updatedAt,
        role = this@toUserLocal.role,
        rating = this@toUserLocal.rating,
        community = this@toUserLocal.community
    )
}