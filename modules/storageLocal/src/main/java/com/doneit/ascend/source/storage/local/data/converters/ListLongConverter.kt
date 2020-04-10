package com.doneit.ascend.source.storage.local.data.converters

import androidx.room.TypeConverter

class ListLongConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInts(values: List<Long>): String {
            return values.joinToString(separator = ",")
        }

        @TypeConverter
        @JvmStatic
        fun toInts(data: String): List<Long> {
            return data.split(",").map { it.toLong() }
        }
    }
}