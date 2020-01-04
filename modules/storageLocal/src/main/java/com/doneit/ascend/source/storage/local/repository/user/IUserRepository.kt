package com.doneit.ascend.source.storage.local.repository.user

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.local.data.UserLocal

interface IUserRepository {
    suspend fun insert(user: UserLocal)
    suspend fun update(user: UserLocal)
    suspend fun getFirstUser(): UserLocal?
    fun getFirstUserLive(): LiveData<UserLocal?>
    suspend fun remove()
}
