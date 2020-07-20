package com.doneit.ascend.source.storage.remote.data.request.group

class CommunityFeedCookies(
    val token: String,
    val community: String
) {
    override fun toString(): String {
        return String.format(COOKIE_TEMPLATE, token, community)
    }

    companion object {
        private const val COOKIE_TEMPLATE = "session_token=%s;community=%s"
    }
}