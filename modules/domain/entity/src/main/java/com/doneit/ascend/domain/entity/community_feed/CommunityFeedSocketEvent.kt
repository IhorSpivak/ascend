package com.doneit.ascend.domain.entity.community_feed

enum class CommunityFeedSocketEvent(
    val command: String
) {
    POST_COMMENTED("PostCommented"),
    POST_LIKED("PostLiked"),
    UNEXPECTED("");

    override fun toString(): String {
        return command
    }

    companion object {
        fun fromRemoteString(representation: String): CommunityFeedSocketEvent {
            return values().firstOrNull { it.command == representation } ?: UNEXPECTED
        }
    }
}