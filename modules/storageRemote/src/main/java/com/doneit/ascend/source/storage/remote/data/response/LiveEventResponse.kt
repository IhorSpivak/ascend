package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

class LiveEventResponse (
    @SerializedName("uri") val link: String,
    @SerializedName("stream_key") val streamKey: String,
    @SerializedName("rtmp_link") val rtmpLink: String
)