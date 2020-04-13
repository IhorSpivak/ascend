package com.doneit.ascend.source.storage.local.data.converters

import androidx.room.TypeConverter

class ListStringConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromString(list: List<String>?): String?{
            return list?.joinToString(separator = ",")
        }

        @TypeConverter
        @JvmStatic
        fun toString(s: String?): List<String>?{
            return s?.split(",")
        }
    }
}