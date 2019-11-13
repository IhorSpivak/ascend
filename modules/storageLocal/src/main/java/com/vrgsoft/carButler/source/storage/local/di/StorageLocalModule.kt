package com.vrgsoft.carButler.source.storage.local.di

import androidx.room.Room
import com.vrgsoft.carButler.source.storage.local.repository.LocalDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object StorageLocalModule {
    fun get() = Kodein.Module("StorageLocalModule") {
        bind<LocalDatabase>() with singleton {
            Room.databaseBuilder(instance(), LocalDatabase::class.java, LocalDatabase.NAME)
                .build()
        }
    }
}