package com.doneit.ascend.source.storage.remote.data.request

data class GroupSocketCookies(
    val token: String,
    val groupId: Long
) {
    override fun toString(): String {
        return String.format(COOKIE_TEMPLATE, token, groupId)
    }

    companion object {
        private const val COOKIE_TEMPLATE = "session_token=%s;group_id=%d"
    }
}