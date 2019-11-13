package com.vrgsoft.carButler.source.storage.local.repository

import androidx.room.RoomDatabase

internal abstract class LocalDatabase : RoomDatabase() {

    companion object {
        const val NAME = "CarButlerDatabase"
    }
}