package com.doneit.ascend.domain.gateway.gateway.notification

import android.content.SharedPreferences

class MockNotificationShared(
    private val booleanType: BooleanType,
    private val intType: IntType
) : SharedPreferences {
    override fun contains(p0: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        return when (booleanType) {
            BooleanType.FALSE -> false
            BooleanType.NULL -> p1
            BooleanType.TRUE -> true
        }
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(p0: String?, p1: Int): Int {
        return when (intType) {
            IntType.NOT_NULL -> 1488
            IntType.NULL -> p1
        }
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(p0: String?, p1: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(p0: String?, p1: String?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    enum class BooleanType {
        NULL, TRUE, FALSE
    }

    enum class IntType {
        NULL, NOT_NULL
    }
}