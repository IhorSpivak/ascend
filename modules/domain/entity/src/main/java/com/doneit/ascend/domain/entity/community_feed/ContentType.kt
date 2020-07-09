package com.doneit.ascend.domain.entity.community_feed

enum class ContentType(val title: String, val mimeType: String) {
    IMAGE("image", "image/*"),
    VIDEO("video", "video/*");
}

fun getContentTypeFromMime(mimeType: String): ContentType {
    for (type in ContentType.values()) {
        if (mimeType.contains(type.title)) {
            return type
        }
    }
    throw IllegalArgumentException("Unsupported mime type: $mimeType")
}