package com.doneit.ascend.source.storage.local.data.converters

import androidx.room.TypeConverter
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.google.gson.Gson

class MembersConverter {

    @TypeConverter
    fun fromMemberList(value: List<MemberLocal>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMemberList(value: String): List<MemberLocal> {
        return Gson().fromJson(value, Array<MemberLocal>::class.java).toMutableList()
    }
}