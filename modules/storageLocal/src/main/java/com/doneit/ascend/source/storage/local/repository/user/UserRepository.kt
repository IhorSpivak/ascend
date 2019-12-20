package com.doneit.ascend.source.storage.local.repository.user

import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.local.repository.LocalDatabase

internal class UserRepository(
    private val database: LocalDatabase
) : IUserRepository {

    override suspend fun insert(user: UserLocal) {
        database.userDao().insert(user)
    }

    override suspend fun update(user: UserLocal) {
        database.userDao().update(user)
    }

    override suspend fun getFirstUser(): UserLocal? {
        return database.userDao().getFirstUser()
    }

    override suspend fun remove() {
        database.userDao().remove()
    }
}