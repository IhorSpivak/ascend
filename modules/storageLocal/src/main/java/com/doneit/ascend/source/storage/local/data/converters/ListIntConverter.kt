package com.doneit.ascend.source.storage.local.data.converters

import androidx.room.TypeConverter

class ListIntConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInts(values: List<Int>?): String {
            val builder = StringBuilder()

            values?.forEach {
                builder.append(it)
                    .append(',')
            }

            return builder.substring(0, builder.length - 1).toString()
        }

        @TypeConverter
        @JvmStatic
        fun toInts(data: String): List<Int>? {
            return data.split(",".toRegex()).map { it.toInt() }
        }
    }
}