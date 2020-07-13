package com.doneit.ascend.source.storage.remote.repository.community_feed.socket

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.group.CommunityFeedCookies
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommunityFeedEventMessage

interface ICommunityFeedSocketRepository {
    val commentStream: LiveData<CommunityFeedEventMessage?>

    fun connect(cookies: CommunityFeedCookies)

    fun disconnect()
}