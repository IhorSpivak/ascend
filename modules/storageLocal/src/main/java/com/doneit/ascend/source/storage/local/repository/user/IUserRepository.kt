package com.doneit.ascend.source.storage.local.repository.user

import com.doneit.ascend.source.storage.local.data.UserLocal

interface IUserRepository {
    suspend fun insert(user: UserLocal)
    suspend fun getFirstUser(): UserLocal
    suspend fun remove(id: Long)
}
