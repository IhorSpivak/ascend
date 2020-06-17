package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class M3u8Response(
    @SerializedName("m3u8_playback_url")
    val m3u8playbackUrl: String
)